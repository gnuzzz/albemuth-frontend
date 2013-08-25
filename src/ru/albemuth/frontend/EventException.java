package ru.albemuth.frontend;

import ru.albemuth.frontend.components.Document;

public class EventException extends FrontendException {

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Document document) {
        super(message, document);
    }

    public EventException(String message, String redirectPath) {
        super(message, redirectPath);
    }

    public EventException(String message, int responseCode) {
        super(message, responseCode);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(String message, Throwable cause, Document document) {
        super(message, cause, document);
    }

    public EventException(String message, Throwable cause, String redirectPath) {
        super(message, cause, redirectPath);
    }

    public EventException(String message, Throwable cause, int responseCode) {
        super(message, cause, responseCode);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    public EventException(Throwable cause, Document document) {
        super(cause, document);
    }

    public EventException(Throwable cause, String redirectPath) {
        super(cause, redirectPath);
    }

    public EventException(Throwable cause, int responseCode) {
        super(cause, responseCode);
    }

}
