package ru.albemuth.frontend.metadata;

import ru.albemuth.frontend.components.Component;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.format.Format;
import ru.albemuth.frontend.components.format.SimpleFormat;

@ContentFile(path = "/ru/albemuth/frontend/components/Label.content")
public abstract class MetadataLabel extends Component {

    public MetadataLabel(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public String getTag() {
        return "span";
    }

    public String getStringValue() {
        String value = getValue();
        return value == null ? "" : value;
    }

    public abstract String getValue();

    public Format getFormat() {
        return SimpleFormat.INSTANCE;
    }

}
