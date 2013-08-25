package ru.albemuth.frontend.components;

import ru.albemuth.frontend.components.format.Format;
import ru.albemuth.frontend.components.format.SimpleFormat;
import ru.albemuth.frontend.components.format.Formatted;

public abstract class Label extends Component<Document> implements Formatted {

    public Label(String name, Component parent, Document document) {
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
