package ru.albemuth.frontend;

import ru.albemuth.frontend.components.Document;

public class RenderException extends FrontendException {

    public RenderException(String message) {
        super(message);
    }

    public RenderException(String message, Document document) {
        super(message, document);
    }

    public RenderException(String message, String redirectPath) {
        super(message, redirectPath);
    }

    public RenderException(String message, int responseCode) {
        super(message, responseCode);
    }

    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RenderException(String message, Throwable cause, Document document) {
        super(message, cause, document);
    }

    public RenderException(String message, Throwable cause, String redirectPath) {
        super(message, cause, redirectPath);
    }

    public RenderException(String message, Throwable cause, int responseCode) {
        super(message, cause, responseCode);
    }

    public RenderException(Throwable cause) {
        super(cause);
    }

    public RenderException(Throwable cause, Document document) {
        super(cause, document);
    }

    public RenderException(Throwable cause, String redirectPath) {
        super(cause, redirectPath);
    }

    public RenderException(Throwable cause, int responseCode) {
        super(cause, responseCode);
    }

}
