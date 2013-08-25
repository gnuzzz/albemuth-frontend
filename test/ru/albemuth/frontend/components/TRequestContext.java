package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.context.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.12.2007
 * Time: 23:54:17
 */
public class TRequestContext extends RequestContext {

    public TRequestContext(Document requestDocument, HttpServletRequest request, HttpServletResponse response) throws RequestException {
        super(request, response);
        setRequestDocument(requestDocument);
        requestDocument.setRequestContext(this);
    }

    public Document getDocument(String path) throws RequestException {
        throw new RequestException("This method is not supported in TRequestContext");
    }

}
