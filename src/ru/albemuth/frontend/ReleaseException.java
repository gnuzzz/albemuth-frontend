package ru.albemuth.frontend;

import ru.albemuth.frontend.components.Document;

public class ReleaseException extends FrontendException {

    public ReleaseException(String message) {
        super(message);
    }

    public ReleaseException(String message, Document document) {
        super(message, document);
    }

    public ReleaseException(String message, String redirectPath) {
        super(message, redirectPath);
    }

    public ReleaseException(String message, int responseCode) {
        super(message, responseCode);
    }

    public ReleaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReleaseException(String message, Throwable cause, Document document) {
        super(message, cause, document);
    }

    public ReleaseException(String message, Throwable cause, String redirectPath) {
        super(message, cause, redirectPath);
    }

    public ReleaseException(String message, Throwable cause, int responseCode) {
        super(message, cause, responseCode);
    }

    public ReleaseException(Throwable cause) {
        super(cause);
    }

    public ReleaseException(Throwable cause, Document document) {
        super(cause, document);
    }

    public ReleaseException(Throwable cause, String redirectPath) {
        super(cause, redirectPath);
    }

    public ReleaseException(Throwable cause, int responseCode) {
        super(cause, responseCode);
    }

}
