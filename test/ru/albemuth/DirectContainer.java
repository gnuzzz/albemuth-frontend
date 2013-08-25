package ru.albemuth;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.components.Document;

public abstract class DirectContainer extends Document {

    public DirectContainer(String name) {
        super(name);
    }

    public Document next() throws EventException {
        return null;
    }

}
