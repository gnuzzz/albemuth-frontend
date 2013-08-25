package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RenderException;
import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.Engine;

public class RedirectDocument extends Document {

    private Document source;

    public RedirectDocument(Document source, String redirectPath) {
        super(RedirectDocument.class.getName());
        setPath(redirectPath);
        this.source = source;
    }

    public Engine.ResponseType getResponseType() {
        return Engine.ResponseType.REDIRECT;
    }

    public void render() throws RenderException {
        throw new RenderException("Redirect document is intended for redirect only!");
    }

    protected void renderContent() throws RenderException {}

    public void release() throws ReleaseException {
        if (source != null) {
            source.release();
        }
    }

}
