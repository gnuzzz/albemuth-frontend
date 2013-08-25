package ru.albemuth.frontend;

import org.apache.log4j.Logger;
import ru.albemuth.frontend.components.Document;

public class FrontendException extends Exception {

    private Document document;
    private String redirectPath;
    private int responseCode;

    public FrontendException(String message) {
        super(message);
    }

    public FrontendException(String message, Document document) {
        super(message);
        this.document = document;
    }

    public FrontendException(String message, String redirectPath) {
        super(message);
        this.redirectPath = redirectPath;
    }

    public FrontendException(String message, int responseCode) {
        super(message);
        this.responseCode = responseCode;
    }

    public FrontendException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrontendException(String message, Throwable cause, Document document) {
        super(message, cause);
        this.document = document;
    }

    public FrontendException(String message, Throwable cause, String redirectPath) {
        super(message, cause);
        this.redirectPath = redirectPath;
    }

    public FrontendException(String message, Throwable cause, int responseCode) {
        super(message, cause);
        this.responseCode = responseCode;
    }

    public FrontendException(Throwable cause) {
        super(cause);
    }

    public FrontendException(Throwable cause, Document document) {
        super(cause);
        this.document = document;
    }

    public FrontendException(Throwable cause, String redirectPath) {
        super(cause);
        this.redirectPath = redirectPath;
    }

    public FrontendException(Throwable cause, int responseCode) {
        super(cause);
        this.responseCode = responseCode;
    }

    public Document getDocument() {
        return document;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void log(String message, Logger logger) {
        logger.error(message, this);
    }

}
