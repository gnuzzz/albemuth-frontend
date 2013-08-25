package ru.albemuth.frontend.components;

import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 27.12.2007
 * Time: 0:10:07
 */
public class TEngine extends Engine {

    private Document requestDocument;

    protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response) {
        try {
            return new TRequestContext(requestDocument, request, response);
        } catch (RequestException e) {
            throw new RuntimeException(e);
        }
    }

    public void setRequestDocument(Document requestDocument) {
        this.requestDocument = requestDocument;
        this.requestDocument.setPath("/" + requestDocument.getName());
    }

}
