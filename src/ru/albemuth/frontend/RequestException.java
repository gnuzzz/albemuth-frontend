package ru.albemuth.frontend;

import ru.albemuth.frontend.components.Document;

public class RequestException extends FrontendException {

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Document document) {
        super(message, document);
    }

    public RequestException(String message, String redirectPath) {
        super(message, redirectPath);
    }

    public RequestException(String message, int responseCode) {
        super(message, responseCode);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(String message, Throwable cause, Document document) {
        super(message, cause, document);
    }

    public RequestException(String message, Throwable cause, String redirectPath) {
        super(message, cause, redirectPath);
    }

    public RequestException(String message, Throwable cause, int responseCode) {
        super(message, cause, responseCode);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    public RequestException(Throwable cause, Document document) {
        super(cause, document);
    }

    public RequestException(Throwable cause, String redirectPath) {
        super(cause, redirectPath);
    }

    public RequestException(Throwable cause, int responseCode) {
        super(cause, responseCode);
    }

}
