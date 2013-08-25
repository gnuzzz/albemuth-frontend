package ru.albemuth.frontend.metadata;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.apache.log4j.Logger;
import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Condition;
import ru.albemuth.frontend.components.Repetition;
import ru.albemuth.frontend.components.Select;
import ru.albemuth.frontend.components.format.Formatted;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConfigurationException;

import java.util.Arrays;
import java.util.Map;

public class ComponentClassBuilderJavassistImpl extends ComponentClassBuilder {

    private static final Logger LOG                             = Logger.getLogger(ComponentClassBuilderJavassistImpl.class);

    private int nextFieldNumber;
    private ClassPool pool;

    public ComponentClassBuilderJavassistImpl(String id) {
        super(id);
    }

    public void configure(Configuration cfg) throws ConfigurationException {
        //ClassPool pool = ClassPool.getDefault();
        super.configure(cfg);
        pool = new ClassPool(null);
        pool.insertClassPath(new ClassClassPath(ComponentClassBuilderJavassistImpl.class));
    }

    public Class<Component> buildComponentClass(ComponentDescriptor componentDescriptor) throws MetadataException {
        try {
            nextFieldNumber = 0;
            Map<ComponentDescriptor, String> sources = getSourcesMap(componentDescriptor);
            LOG.debug(componentDescriptor.getComponentClass().getName() + "Impl" + componentDescriptor.getName());
            CtClass clazz = pool.makeClass(componentDescriptor.getComponentClass().getName() + "Impl" + componentDescriptor.getName());
            LOG.debug("extends " + componentDescriptor.getComponentClass().getName());
            CtClass superClass = pool.get(componentDescriptor.getComponentClass().getName());
            clazz.setSuperclass(superClass);

            for (ComponentDescriptor cd: sources.keySet()) {
                LOG.debug("private " + cd.getComponentImplClass().getName() + " " + sources.get(cd) + ";");
                CtField sourceField = CtField.make("private " + cd.getComponentImplClass().getName() + " " + sources.get(cd) + ";", clazz);
                clazz.addField(sourceField);
                LOG.debug("public void " + getPropertyMethodName("set", sources.get(cd)) + "(" + cd.getComponentImplClass().getName() + " source) {" + sources.get(cd) + " = source;}");
                CtMethod sourceSetterMethod = CtMethod.make("public void " + getPropertyMethodName("set", sources.get(cd)) + "(" + cd.getComponentImplClass().getName() + " source) {" + sources.get(cd) + " = source;}", clazz);
                clazz.addMethod(sourceSetterMethod);
            }

            for (LinkDescriptor link: componentDescriptor.getLinks()) {
                CtMethod superClassGetter = getGetter(superClass, link.getDestination().getName());
                String methodSourceCode;
                if (superClassGetter != null) {
                    //if ((superClassGetter.getModifiers() & Modifier.ABSTRACT) != 0) {
                    methodSourceCode = "public " + superClassGetter.getReturnType().getName() + " " + superClassGetter.getName() + "() {\n";
                    if (link.getSource() instanceof PropertyDescriptor) {
                        methodSourceCode += "return " + processPropertyDescriptorGetter(pool, clazz, superClassGetter.getReturnType(), sources, (PropertyDescriptor)link.getSource(), link.getDestination());
                    } else if (link.getSource() instanceof ComplexPropertyDescriptor) {
                        ComponentDescriptor complexPropertyOwnerDescriptor = ((ComplexPropertyDescriptor)link.getSource()).getOwner();
                        if (complexPropertyOwnerDescriptor != null) {
                            String formatParameterString = "";
                            if (Formatted.class.isAssignableFrom(link.getDestination().getComponentDescriptor().getComponentClass()) && link.getDestination().getName().equals("value")) {//todo: this is a dirty hack
                                formatParameterString = "getFormat()";
                            }
                            methodSourceCode += "return " + sources.get(complexPropertyOwnerDescriptor) + "." + ((ComplexPropertyDescriptor)link.getSource()).getMethodName() + "(" + formatParameterString + ");\n";
                        } else {
                            methodSourceCode += "return " + processComplexPropertyDescriptorGetter(pool, clazz, superClassGetter.getReturnType(), sources, (ComplexPropertyDescriptor)link.getSource(), link.getDestination());
                        }
                    } else if (link.getSource() instanceof ConstantValueDescriptor) {
                        methodSourceCode += "return " + processConstValueDescriptorGetter(pool, clazz, superClassGetter.getReturnType(), sources, (ConstantValueDescriptor)link.getSource(), link.getDestination());
                    } else if (link.getSource() instanceof PropertiesArrayDescriptor) {
                        methodSourceCode += processPropertyArrayDescriptorGetter(pool, clazz, superClassGetter.getReturnType(), sources, (PropertiesArrayDescriptor)link.getSource(), link.getDestination());
                    } else {
                        throw new MetadataException("Can't create link " + link + ": Unknown source link side descriptor: " + link.getSource().getClass().getName());
                    }
                    methodSourceCode += "}";
                    LOG.debug(methodSourceCode);
                    CtMethod getterMethod = CtMethod.make(methodSourceCode, clazz);
                    clazz.addMethod(getterMethod);
                    //}

                    CtMethod superClassSetter = getSetter(superClass, link.getDestination().getName(), superClassGetter.getReturnType());
                    if (superClassSetter != null) {
                        //if ((superClassSetter.getModifiers() & Modifier.ABSTRACT) != 0) {
                        methodSourceCode = "public void " + superClassSetter.getName() + "(" + superClassSetter.getParameterTypes()[0].getName() + " value) {\n";
                        if (link.getSource() instanceof PropertyDescriptor) {
                            methodSourceCode += processPropertyDescriptorSetter(pool, clazz, superClassSetter.getParameterTypes()[0], sources, (PropertyDescriptor)link.getSource(), link.getDestination(), "value");
                        } else if (link.getSource() instanceof ComplexPropertyDescriptor) {
                            methodSourceCode += processComplexPropertyDescriptorSetter(pool, clazz, superClassSetter.getParameterTypes()[0], sources, (ComplexPropertyDescriptor)link.getSource(), link.getDestination(), "value");
                        } else if (link.getSource() instanceof ConstantValueDescriptor) {
                            methodSourceCode += processConstValueDescriptorSetter(pool, clazz, superClassSetter.getParameterTypes()[0], sources, (ConstantValueDescriptor)link.getSource(), link.getDestination(), "value");
                        } else if (link.getSource() instanceof PropertiesArrayDescriptor) {
                            methodSourceCode += processPropertyArrayDescriptorSetter(pool, clazz, superClassSetter.getParameterTypes()[0], sources, (PropertiesArrayDescriptor)link.getSource(), link.getDestination(), "value");
                        } else {
                            throw new MetadataException("Can't create link " + link + ": Unknown link side descriptor: " + link.getSource().getClass().getName());
                        }
                        methodSourceCode += "}";
                        LOG.debug(methodSourceCode);
                        CtMethod setterMethod = CtMethod.make(methodSourceCode, clazz);
                        clazz.addMethod(setterMethod);
                        //}
                    }
                } else {
                    throw new MetadataException("Can't create class for component " + componentDescriptor + ": can't create link " + link + ": no getter for destination property");
                }

            }

            for (LinkDescriptor link: componentDescriptor.getComplexPropertiesLinks()) {
                if (link.getSource() instanceof ComplexPropertyDescriptor) {
                    CtClass destinationPropertyClass = pool.get(link.getDestination().getComponentDescriptor().getComponentClass().getName());
                    CtMethod destinationPropertyGetter = getGetter(destinationPropertyClass, link.getDestination().getName());

                    String formatParameterString = "";
                    if (Formatted.class.isAssignableFrom(link.getDestination().getComponentDescriptor().getComponentClass()) && link.getDestination().getName().equals("value")) {//todo: this is a dirty hack
                        formatParameterString = "ru.albemuth.frontend.components.format.Format format";
                    }

                    String complexMethodSourceCode = "public final " + destinationPropertyGetter.getReturnType().getName() + " " + ((ComplexPropertyDescriptor)link.getSource()).getMethodName() + "(" + formatParameterString + ") {\n";
                    String retSource = processComplexPropertyDescriptorGetter(pool, clazz, destinationPropertyGetter.getReturnType(), sources, (ComplexPropertyDescriptor)link.getSource(), link.getDestination());
                    complexMethodSourceCode += "return " + (formatParameterString.length() > 0 ? "format.getStringValue(" + retSource.substring(0, retSource.length() - 2) + ");" : retSource);
                    complexMethodSourceCode += "}";
                    LOG.debug(complexMethodSourceCode);
                    CtMethod complexPropertyMethod = CtMethod.make(complexMethodSourceCode, clazz);
                    clazz.addMethod(complexPropertyMethod);
                } else if (link.getSource() instanceof PropertiesArrayDescriptor) {
                    PropertiesArrayDescriptor sourcePropertyArray = (PropertiesArrayDescriptor)link.getSource();
                    for (int i = 0; i < sourcePropertyArray.getSources().length; i++) {
                        if (sourcePropertyArray.getSources()[i] instanceof ComplexPropertyDescriptor) {
                            CtClass destinationPropertyClass = pool.get(link.getDestination().getComponentDescriptor().getComponentClass().getName());
                            CtMethod destinationPropertyGetter = getGetter(destinationPropertyClass, link.getDestination().getName());
                            String complexMethodSourceCode = "public final " + destinationPropertyGetter.getReturnType().getComponentType().getName() + " " + ((ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i]).getMethodName() + "() {\n";
                            complexMethodSourceCode += "return " + processComplexPropertyDescriptorGetter(pool, clazz, destinationPropertyGetter.getReturnType().getComponentType(), sources, (ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i], link.getDestination());
                            complexMethodSourceCode += "}";
                            LOG.debug(complexMethodSourceCode);
                            CtMethod complexPropertyMethod = CtMethod.make(complexMethodSourceCode, clazz);
                            clazz.addMethod(complexPropertyMethod);
                        }
                    }
                }
            }

            String setContextPropertiesValuesSource = "";
            String releaseSource = "";
            for (CtMethod method: superClass.getMethods()) {
                Annotation contextPropertyAnnotation = method.getMethodInfo().getAttribute(AnnotationsAttribute.invisibleTag) != null ? ((AnnotationsAttribute)method.getMethodInfo().getAttribute(AnnotationsAttribute.invisibleTag)).getAnnotation(ContextProperty.class.getName()) : null;
                if (contextPropertyAnnotation != null && (clazz.getMethod(method.getName(), method.getSignature()).getModifiers() & Modifier.ABSTRACT) == 0) {
                    if (method.getParameterTypes().length != 1 || !method.getName().startsWith("set")) {
                        throw new MetadataException("Method " + method.getName() + " of component " + superClass.getName() + " annotated as context property isn't a property setter (it should has one parameter and starts with \"set\")");
                    }
                    String propertyName = method.getName().substring("set".length(), "set".length() + 1).toLowerCase() + method.getName().substring("set".length() + 1);
                    String propertyContextName = contextPropertyAnnotation.getMemberValue("contextName") != null && !ContextProperty.DEFAULT_CONTEXT_NAME.equals(((StringMemberValue)contextPropertyAnnotation.getMemberValue("contextName")).getValue()) ? ((StringMemberValue)contextPropertyAnnotation.getMemberValue("contextName")).getValue() : propertyName;
                    CtClass propertyClass = method.getParameterTypes()[0];
                    setContextPropertiesValuesSource += method.getName() + "(";
                    if (((EnumMemberValue)contextPropertyAnnotation.getMemberValue("type")).getValue().equals(ContextType.REQUEST.name())) {
                        setContextPropertiesValuesSource += getPropertyValueSetterSourceForStringContext(propertyContextName, propertyClass);
                    } else if (((EnumMemberValue)contextPropertyAnnotation.getMemberValue("type")).getValue().equals(ContextType.PROCESS.name())) {
                        setContextPropertiesValuesSource += getPropertyValueSetterSourceForObjectContext(propertyContextName, propertyClass);
                    } else if (((EnumMemberValue)contextPropertyAnnotation.getMemberValue("type")).getValue().equals(ContextType.SESSION.name())) {
                        setContextPropertiesValuesSource += getPropertyValueSetterSourceForObjectContext(propertyContextName, propertyClass);
                    } else if (((EnumMemberValue)contextPropertyAnnotation.getMemberValue("type")).getValue().equals(ContextType.APPLICATION.name())) {
                        setContextPropertiesValuesSource += getPropertyValueSetterSourceForObjectContext(propertyContextName, propertyClass);
                    } else {
                        throw new MetadataException("Unknown context property type " + ((EnumMemberValue)contextPropertyAnnotation.getMemberValue("type")).getValue() + " for property " + propertyName + " of component " + superClass.getName());
                    }
                    setContextPropertiesValuesSource += ");\n";
                    releaseSource += method.getName() + "(" + getReleasePropertySource(propertyClass) + ");\n";
                }
            }
            if (setContextPropertiesValuesSource.length() > 0) {
                setContextPropertiesValuesSource = "protected void setContextPropertiesValues() throws ru.albemuth.frontend.RequestException {\ntry {\n" + setContextPropertiesValuesSource + "} catch (ru.albemuth.util.ConvertorException e) {\nthrow new ru.albemuth.frontend.RequestException(\"Can't set context properties values for component \" + getName(), e);\n}\n super.setContextPropertiesValues();\n}";
                LOG.debug(setContextPropertiesValuesSource);
                CtMethod setContextPropertiesValuesMethod = CtMethod.make(setContextPropertiesValuesSource, clazz);
                clazz.addMethod(setContextPropertiesValuesMethod);
            }
            if (releaseSource.length() > 0) {
                releaseSource = "protected void releaseProperties() throws ru.albemuth.frontend.ReleaseException {\n" + releaseSource + " super.releaseProperties();\n}";
                LOG.debug(releaseSource);
                CtMethod releaseMethod = CtMethod.make(releaseSource, clazz);
                clazz.addMethod(releaseMethod);
            }

            ListenerDescriptor listenerDescriptor = componentDescriptor.getListener();
            if (listenerDescriptor != null) {
                String processActionSource = "protected ru.albemuth.frontend.components.Document processAction() throws ru.albemuth.frontend.EventException {\n";
                processActionSource += "return " + sources.get(listenerDescriptor.getTargetDescriptor()) + "." + listenerDescriptor.getTargetMethodName() + "();\n}";
                LOG.debug(processActionSource);
                CtMethod processActionMethod = CtMethod.make(processActionSource, clazz);
                clazz.addMethod(processActionMethod);
            }

            String renderContentSource = "protected void renderContent() throws ru.albemuth.frontend.RenderException {\n";
            ComponentDescriptor[] componentChildren = componentDescriptor.getChildren();
            for (int i = 0, childIndex = 0; i < componentChildren.length; i++) {
                if (componentChildren[i] instanceof ConstantTextDescriptor) {
                    renderContentSource += "getRequestContext().getResponse().append(\"" + ((ConstantTextDescriptor)componentChildren[i]).getContent().replace("\"", "\\\"").replaceAll("\\n", "\\\\n\"+\n\"") + "\");\n";
                } else if (componentChildren[i] instanceof PropertyValueDescriptor) {
                    PropertyValueDescriptor child = (PropertyValueDescriptor)componentChildren[i];
                    if (child.getLinks()[0].getSource() instanceof PropertyDescriptor) {
                        PropertyDescriptor pd = (PropertyDescriptor)child.getLinks()[0].getSource();
                        if (pd.getComponentDescriptor() == componentDescriptor) {
                            renderContentSource += "getRequestContext().getResponse().append(" + processPropertyValueDescriptorGetter(pool, sources, pd) + ");\n";//todo:
                        } else {
                            renderContentSource += "getRequestContext().getResponse().append(";
                            renderContentSource += processPropertyValueDescriptorGetter(pool, sources, pd) + ");\n";
                        }
                    } else {
                        throw new MetadataException("Can't create class for component " + componentDescriptor + ": nested property value descriptor isn't a property-to-property descriptor");
                    }
                } else if (componentChildren[i] instanceof ComplexPropertyValueDescriptor) {
                    ComplexPropertyValueDescriptor child = (ComplexPropertyValueDescriptor)componentChildren[i];
                    String source = "";
                    for (ComponentDescriptor cd: child.getChildren()) {
                        if (cd instanceof ConstantTextDescriptor) {
                            source += ((ConstantTextDescriptor)cd).getContent();
                        } else if (cd instanceof PropertyValueDescriptor) {
                            PropertyValueDescriptor pvd = (PropertyValueDescriptor)cd;
                            if (pvd.getLinks()[0].getSource() instanceof PropertyDescriptor) {
                                PropertyDescriptor pd = (PropertyDescriptor)pvd.getLinks()[0].getSource();
                                if (pd.getComponentDescriptor() == componentDescriptor) {
                                    source += processPropertyValueDescriptorGetter(pool, sources, pd);//todo: см todo у PropertyValueDescriptor выше
                                } else {
                                    source += processPropertyValueDescriptorGetter(pool, sources, pd);
                                }
                            } else {
                                throw new MetadataException("Can't create class for component " + componentDescriptor + ": nested property value descriptor isn't a property-to-property descriptor");
                            }
                        }
                    }
                    renderContentSource += "getRequestContext().getResponse().append((" + source + "));\n";
                } else if (!(componentChildren[i] instanceof NestedContentDescriptor)) {
                    renderContentSource += "children[" + childIndex + "].render();\n";
                    childIndex++;
                }
            }
            renderContentSource += "}\n";
            LOG.debug(renderContentSource);
            CtMethod renderContentMethod = CtMethod.make(renderContentSource, clazz);
            clazz.addMethod(renderContentMethod);

            Class ret = clazz.toClass();
            if (!Component.class.isAssignableFrom(ret)) {
                throw new MetadataException("Can't create class for component " + componentDescriptor + ": class cast exception: class " + componentDescriptor.getComponentClass().getName() + " isn't a subclass of Component");
            }
            componentDescriptor.setComponentImplClass(ret);
            return ret;
        } catch (NotFoundException e) {
            throw new MetadataException("Can't create class for component " + componentDescriptor, e);
        } catch (CannotCompileException e) {
            throw new MetadataException("Can't create class for component " + componentDescriptor, e);
        }
    }

    private String processPropertyValueDescriptorGetter(ClassPool pool, Map<ComponentDescriptor, String> sources, PropertyDescriptor sourceProperty) throws MetadataException {//todo rename
        return processPropertyValueDescriptorGetter(pool, sources, sourceProperty, null);
    }

    private String processPropertyValueDescriptorGetter(ClassPool pool, Map<ComponentDescriptor, String> sources, PropertyDescriptor sourceProperty, PropertyDescriptor destinationProperty) throws MetadataException {//todo rename
        try {
            String[] propertiesNames = sourceProperty.getName().split("\\.");
            String sourceCode = sources.get(sourceProperty.getComponentDescriptor());
            sourceCode = sourceCode == null ? "this" : sourceCode;//todo: wtf???
            CtClass currentSourceClass = pool.get(sourceProperty.getComponentDescriptor().getComponentClass().getName());
            for (String propertyName: propertiesNames) {
                CtMethod getter = getGetter(currentSourceClass, propertyName);
                if (getter != null) {
                    sourceCode += "." + getter.getName() + "()";
                    currentSourceClass = getter.getReturnType();
                } else {
                    CtField field = getField(currentSourceClass, propertyName);
                    if (field != null) {
                        sourceCode += "." + field.getName();
                        currentSourceClass = field.getType();
                    } else {
                        throw new MetadataException("Cant process sourceProperty descriptor getter for sourceProperty " + sourceProperty + ": sourceProperty hasn't getter or field available");
                    }
                }
            }
            if (destinationProperty != null && Condition.class.isAssignableFrom(destinationProperty.getComponentDescriptor().getComponentClass())) {//todo: this is dirty hack
                if (currentSourceClass.isPrimitive()) {
                    if (!currentSourceClass.equals(CtClass.booleanType)) {
                        sourceCode += " > 0";
                    }
                } else if (currentSourceClass.getName().equals(String.class.getName())) {
                    sourceCode = sourceCode + " != null && " + sourceCode + ".length() > 0";
                } else {
                    sourceCode += " != null";
                }
            }
            //if (currentSourceClass.equals(pool.get(destinationProperty.getComponentDescriptor())))
            return sourceCode;
        } catch (NotFoundException e) {
            throw new MetadataException("Cant process source sourceProperty descriptor getter for sourceProperty " + sourceProperty, e);
        }
    }

    private String processPropertyDescriptorGetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, PropertyDescriptor sourceProperty, PropertyDescriptor destinationProperty) throws MetadataException {
        try {
            String source = processPropertyValueDescriptorGetter(pool, sources, sourceProperty, destinationProperty);
            if (Formatted.class.isAssignableFrom(destinationProperty.getComponentDescriptor().getComponentClass()) && destinationProperty.getName().equals("value")) {//todo: this is a dirty hack
                source = "getFormat().getStringValue(" + source + ")";
            }
            return source + ";\n";
        } catch (MetadataException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": cant process source property descriptor getter for property " + sourceProperty, e);
        }
    }

    private String processComplexPropertyDescriptorGetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, ComplexPropertyDescriptor sourceProperty, PropertyDescriptor destinationProperty) throws MetadataException {
        try {
            String source = "";
            for (LinkSide ls: sourceProperty.getSources()) {
                if (ls instanceof PropertyDescriptor) {
                    source += processPropertyValueDescriptorGetter(pool, sources, (PropertyDescriptor)ls, null);
                } else if (ls instanceof ConstantValueDescriptor) {
                    source += ((ConstantValueDescriptor)ls).getConstantValue();
                }
            }
            /*if (Formatted.class.isAssignableFrom(destinationProperty.getComponentDescriptor().getComponentClass()) && destinationProperty.getName().equals("value")) {//todo: this is a dirty hack
                source = "getFormat().getStringValue(" + source + ")";
            }*/
            return source + ";\n";
        } catch (MetadataException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": cant process complex source property descriptor getter for property " + sourceProperty, e);
        }
    }

    private String processConstValueDescriptorGetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, ConstantValueDescriptor sourceValue, PropertyDescriptor destinationProperty) throws MetadataException {
        try {
            int fieldNumber = getNextUniqueFieldNumber();
            LOG.debug("private final " + propertyClass.getName() + " " + destinationProperty.getName() + "Constant" + fieldNumber + " = " + getPropertyValueParserSource(destinationProperty.getName(), propertyClass, sourceValue.getConstantValue() != null ? "\"" + sourceValue.getConstantValue() + "\"" : null) + ";");
            CtField sourceField = CtField.make("private final " + propertyClass.getName() + " " + destinationProperty.getName() + "Constant" + fieldNumber + " = " + getPropertyValueParserSource(destinationProperty.getName(), propertyClass, sourceValue.getConstantValue() != null ? "\"" + sourceValue.getConstantValue() + "\"" : null) + ";", clazz);
            clazz.addField(sourceField);
            return destinationProperty.getName() + "Constant" + fieldNumber + ";\n";
        } catch (CannotCompileException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source const value descriptor getter for value " + sourceValue, e);
        }
    }

    private String processPropertyArrayDescriptorGetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, PropertiesArrayDescriptor sourcePropertyArray, PropertyDescriptor destinationProperty) throws MetadataException {
        try {
            String sourceCode = "";
            LOG.debug("private final " + propertyClass.getName() + " " + destinationProperty.getName() + "Array = new " + propertyClass.getName().substring(0, propertyClass.getName().length() - 1) + sourcePropertyArray.getSources().length + "];");
            CtField sourceField = CtField.make("private final " + propertyClass.getName() + " " + destinationProperty.getName() + "Array = new " + propertyClass.getName().substring(0, propertyClass.getName().length() - 1) + sourcePropertyArray.getSources().length + "];", clazz);
            clazz.addField(sourceField);
            for (int i = 0; i < sourcePropertyArray.getSources().length; i++) {
                if (sourcePropertyArray.getSources()[i] instanceof PropertyDescriptor) {
                    sourceCode += destinationProperty.getName() + "Array[" + i + "] = " + processPropertyDescriptorGetter(pool, clazz, propertyClass.getComponentType(), sources, (PropertyDescriptor)sourcePropertyArray.getSources()[i], destinationProperty);
                } else if (sourcePropertyArray.getSources()[i] instanceof ComplexPropertyDescriptor) {
                    ComponentDescriptor complexPropertyOwnerDescriptor = ((ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i]).getOwner();
                    if (complexPropertyOwnerDescriptor != null) {
                        sourceCode += destinationProperty.getName() + "Array[" + i + "] = " + sources.get(complexPropertyOwnerDescriptor) + "." + ((ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i]).getMethodName() + "();\n";
                    } else {
                        sourceCode += destinationProperty.getName() + "Array[" + i + "] = " + processComplexPropertyDescriptorGetter(pool, clazz, propertyClass.getComponentType(), sources, (ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i], destinationProperty);
                    }
                } else if (sourcePropertyArray.getSources()[i] instanceof ConstantValueDescriptor) {
                    sourceCode += destinationProperty.getName() + "Array[" + i + "] = " + processConstValueDescriptorGetter(pool, clazz, propertyClass.getComponentType(), sources, (ConstantValueDescriptor)sourcePropertyArray.getSources()[i], destinationProperty);
                } else {
                    throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source property array descriptor getter for  " + sourcePropertyArray + ": unknown source " + sourcePropertyArray.getSources()[i]);
                }
            }
            sourceCode += "return " + destinationProperty.getName() + "Array;\n";
            return sourceCode;
        } catch (CannotCompileException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source property array descriptor getter for " + sourcePropertyArray, e);
        } catch (NotFoundException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source property array descriptor getter for " + sourcePropertyArray, e);
        }
    }

    private String processPropertyDescriptorSetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, PropertyDescriptor sourceProperty, PropertyDescriptor destinationProperty, String valueName) throws MetadataException {
        try {
            String[] sourcePropertiesNames = sourceProperty.getName().split("\\.");
            String sourceCode = sources.get(sourceProperty.getComponentDescriptor());
            CtClass currentSourceClass = pool.get(sourceProperty.getComponentDescriptor().getComponentClass().getName());
            for (int i = 0; i < sourcePropertiesNames.length - 1; i++) {
                CtMethod getter = getGetter(currentSourceClass, sourcePropertiesNames[i]);
                if (getter != null) {
                    sourceCode += "." + getter.getName() + "()";
                    currentSourceClass = getter.getReturnType();
                } else {
                    CtField field = getField(currentSourceClass, sourcePropertiesNames[i]);
                    if (field != null) {
                        sourceCode += "." + field.getName();
                        currentSourceClass = field.getType();
                    } else {
                        throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't create setter for destination property: source property hasn't setter or field available");
                    }
                }
            }
            CtMethod getter = getGetter(currentSourceClass, sourcePropertiesNames[sourcePropertiesNames.length - 1]);
            CtMethod setter = getSetter(currentSourceClass, sourcePropertiesNames[sourcePropertiesNames.length - 1], getter.getReturnType());
            if (Formatted.class.isAssignableFrom(destinationProperty.getComponentDescriptor().getComponentClass()) && destinationProperty.getName().equals("value")) {//todo: this is a dirty hack
                String valueTypeName = getPropertyTypeNameForParserMethodName(getter.getReturnType());
                if (valueTypeName != null) {
                    valueName = "getFormat().get" + valueTypeName + "Value("  + valueName + ")";
                } else {
                    valueName = "getFormat().getObjectValue("  + valueName + ")";
                }
                //source = "getFormat().getStringValue(" + source + ")";
            }
            if (setter != null) {
                sourceCode += "." + setter.getName() + "(" + (isRepetitionItem(destinationProperty) ? "(" + getter.getReturnType().getName() + ")" : "") + valueName + ")";
            } else {
                CtField field = getField(currentSourceClass, sourcePropertiesNames[sourcePropertiesNames.length - 1]);
                if (field != null) {
                    sourceCode += "." + field.getName() + " = " + (isRepetitionItem(destinationProperty) ? "(" + field.getType().getName() + ")" : "") + valueName;
                } else {
                    throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": cant' create setter for source property " + sourceProperty.getName() + ", destination property " + destinationProperty.getName());
                }
            }
            sourceCode += ";\n";
            return sourceCode;
        } catch (NotFoundException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": cant process source property descriptor setter for property " + sourceProperty, e);
        }
    }

    private boolean isRepetitionItem(PropertyDescriptor propertyDescriptor) {
        return (Repetition.class.isAssignableFrom(propertyDescriptor.getComponentDescriptor().getComponentClass()) || Select.class.isAssignableFrom(propertyDescriptor.getComponentDescriptor().getComponentClass())) && ("item".equals(propertyDescriptor.getName()) || "selectedItem".equals(propertyDescriptor.getName()));
    }

    private String processComplexPropertyDescriptorSetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, ComplexPropertyDescriptor sourceProperty, PropertyDescriptor destinationProperty, String valueName) throws MetadataException {
        return "";//do nothing
    }

    private String processConstValueDescriptorSetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, ConstantValueDescriptor sourceValue, PropertyDescriptor destinationProperty, String valueName) throws MetadataException {
        return "";//do nothing
    }

    private String processPropertyArrayDescriptorSetter(ClassPool pool, CtClass clazz, CtClass propertyClass, Map<ComponentDescriptor, String> sources, PropertiesArrayDescriptor sourcePropertyArray, PropertyDescriptor destinationProperty, String valueName) throws MetadataException {
        try {
            String sourceCode = "";
            for (int i = 0; i < sourcePropertyArray.getSources().length; i++) {
                if (sourcePropertyArray.getSources()[i] instanceof PropertyDescriptor) {
                    sourceCode += processPropertyDescriptorSetter(pool, clazz, propertyClass.getComponentType(), sources, (PropertyDescriptor)sourcePropertyArray.getSources()[i], destinationProperty, valueName + "[" + i + "]");
                } else if (sourcePropertyArray.getSources()[i] instanceof ComplexPropertyDescriptor) {
                    sourceCode += processComplexPropertyDescriptorSetter(pool, clazz, propertyClass.getComponentType(), sources, (ComplexPropertyDescriptor)sourcePropertyArray.getSources()[i], destinationProperty, valueName + "[" + i + "]");
                } else if (sourcePropertyArray.getSources()[i] instanceof ConstantValueDescriptor) {
                    sourceCode += processConstValueDescriptorSetter(pool, clazz, propertyClass.getComponentType(), sources, (ConstantValueDescriptor)sourcePropertyArray.getSources()[i], destinationProperty, valueName + "[" + i + "]");
                } else {
                    throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source property array descriptor setter for  " + sourcePropertyArray + ": unknown source " + sourcePropertyArray.getSources()[i]);
                }
            }
            return sourceCode;
        } catch (NotFoundException e) {
            throw new MetadataException("Can't create class for component " + destinationProperty.getComponentDescriptor() + ": can't process source property array descriptor setter for " + sourcePropertyArray, e);
        }
    }

    private CtMethod getGetter(CtClass clazz, String propertyName) throws NotFoundException {
        CtMethod getter = getMethod(clazz, getPropertyMethodName("get", propertyName));
        if (getter == null) {
            getter = getMethod(clazz, getPropertyMethodName("is", propertyName));
            if (getter == null || (!getter.getReturnType().equals(CtClass.booleanType) && !getter.getReturnType().equals(clazz.getClassPool().get(Boolean.class.getName())))) {
                getter = getMethod(clazz, propertyName);
            }
        }
        return getter;
    }

    private CtMethod getSetter(CtClass clazz, String propertyName, CtClass parameterType) throws NotFoundException {
        return getMethod(clazz, getPropertyMethodName("set", propertyName), parameterType);
    }

    private CtMethod getMethod(CtClass clazz, String methodName, CtClass... parameterTypes) throws NotFoundException {
        CtMethod method = null;
        for (CtMethod m: clazz.getDeclaredMethods()) {
            if (m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), parameterTypes)) {
                method = m;
                break;
            }
        }
        if (method == null) {
            for (CtMethod m: clazz.getMethods()) {
                if (m.getName().equals(methodName) && Arrays.equals(m.getParameterTypes(), parameterTypes)) {
                    method = m;
                    break;
                }
            }
        }
        return method;
    }

    private CtField getField(CtClass clazz, String propertyName) {
        try {
            return clazz.getDeclaredField(propertyName);
        } catch (NotFoundException e) {
            return null;
        }
    }

    private String getPropertyValueParserSource(String propertyName, CtClass propertyClass, String propertyValueSource) {
        String source;
        String propertyTypeName = getPropertyTypeNameForParserMethodName(propertyClass);
        if ("String".equals(propertyTypeName)) {
            source = propertyValueSource;
        } else if ("Int".equals(propertyTypeName)) {
            source = "ru.albemuth.util.Convertor.parseIntValue(" + propertyValueSource + ")";
            source = propertyClass.isPrimitive() ? source : "Integer.valueOf(" + source + ")";
        } else if (propertyTypeName != null) {
            source = "ru.albemuth.util.Convertor.parse" + propertyTypeName + "Value(" + propertyValueSource + ")";
            source = propertyClass.isPrimitive() ? source : propertyTypeName + ".valueOf(" + source + ")";
        } else {
            source = getPropertyMethodName("parse", propertyName + "Value(") + propertyValueSource + ")";
        }
        return source;
    }

    private String getPropertyTypeNameForParserMethodName(CtClass propertyClass) {
        String typeName = null;
        if (propertyClass.getName().equals(String.class.getName())) {
            typeName = "String";
        } else if (propertyClass.equals(CtClass.booleanType) || propertyClass.getName().equals(Boolean.class.getName())) {
            typeName = "Boolean";
        } else if (propertyClass.equals(CtClass.byteType) || propertyClass.getName().equals(Byte.class.getName())) {
            typeName = "Byte";
        } else if (propertyClass.equals(CtClass.charType) || propertyClass.getName().equals(Character.class.getName())) {
            typeName = "Char";
        } else if (propertyClass.equals(CtClass.shortType) || propertyClass.getName().equals(Short.class.getName())) {
            typeName = "Short";
        } else if (propertyClass.equals(CtClass.intType) || propertyClass.getName().equals(Integer.class.getName())) {
            typeName = "Int";
        } else if (propertyClass.equals(CtClass.longType) || propertyClass.getName().equals(Long.class.getName())) {
            typeName = "Long";
        } else if (propertyClass.equals(CtClass.floatType) || propertyClass.getName().equals(Float.class.getName())) {
            typeName = "Float";
        } else if (propertyClass.equals(CtClass.doubleType) || propertyClass.getName().equals(Double.class.getName())) {
            typeName = "Double";
        }
        return typeName;
    }

    private String getPropertyValueSetterSourceForStringContext(String propertyContextName, CtClass propertyClass) {
        //return getPropertyValueParserSource(propertyContextName, propertyClass, "(String)getRequestContext().getParameterValue(getName() + \"." + propertyContextName + "\")");
        return getPropertyValueParserSource(propertyContextName, propertyClass, "(String)getRequestContext().getParameterValue(\"" + propertyContextName + "\")");
    }

    private String getPropertyValueSetterSourceForObjectContext(String propertyContextName, CtClass propertyClass) {
        //String source = "getProcessContext().getParameterValue(getName() + \"." + propertyContextName + "\")";
        String source = "getProcessContext().getParameterValue(\"" + propertyContextName + "\")";
        if (propertyClass.equals(CtClass.booleanType)) {
            source = "((Boolean)" + source + ").booleanValue()";
        } else if (propertyClass.equals(CtClass.byteType)) {
            source = "((Byte)" + source + ").byteValue()";
        } else if (propertyClass.equals(CtClass.charType)) {
            source = "((Character)" + source + ").charValue()";
        } else if (propertyClass.equals(CtClass.shortType)) {
            source = "((Short)" + source + ").shortValue()";
        } else if (propertyClass.equals(CtClass.intType)) {
            source = "((Integer)" + source + ").intValue()";
        } else if (propertyClass.equals(CtClass.longType)) {
            source = "((Long)" + source + ").longValue()";
        } else if (propertyClass.equals(CtClass.floatType)) {
            source = "((Float)" + source + ").floatValue()";
        } else if (propertyClass.equals(CtClass.doubleType)) {
            source = "((Double)" + source + ").doubleValue()";
        } else {
            source = "(" + propertyClass.getName() + ")" + source;
        }
        return source;
    }

    private String getReleasePropertySource(CtClass propertyClass) {
        String source = "";
        if (propertyClass.equals(CtClass.booleanType)) {
            source += "false";
        } else if (propertyClass.equals(CtClass.byteType)) {
            source += "(byte)0";
        } else if (propertyClass.equals(CtClass.charType)) {
            source += "(char)0";
        } else if (propertyClass.equals(CtClass.shortType)) {
            source += "(short)0";
        } else if (propertyClass.equals(CtClass.intType)) {
            source += "0";
        } else if (propertyClass.equals(CtClass.longType)) {
            source += "0";
        } else if (propertyClass.equals(CtClass.floatType)) {
            source += "0";
        } else if (propertyClass.equals(CtClass.doubleType)) {
            source += "0";
        } else {
            source += "null";
        }
        return source;
    }

    private int getNextUniqueFieldNumber() {
        return nextFieldNumber++;
    }

}
