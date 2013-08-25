package ru.albemuth.frontend.context;

import ru.albemuth.frontend.ReleaseException;
import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.components.ActionComponent;
import ru.albemuth.frontend.components.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public abstract class RequestContext extends Context<String> {

    private static final ThreadLocal<RequestContext> INSTANCE       = new ThreadLocal<RequestContext>();

    private String encoding = "Windows-1251";
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private Map<String, String[]> parameters;
    private StringBuilder response = new StringBuilder();
    private Document requestDocument;
    private Document responseDocument;
    private ActionComponent eventSource;
    private boolean inSourceEventForm;
    private URL uri;
    private String itemid;
    private SessionContext sessionContext;
    private ProcessContext processContext;
    private Engine.Mode mode;
    private Engine.ResponseType responseType = Engine.ResponseType.FORWARD;

    @SuppressWarnings("unchecked")
    public RequestContext(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws RequestException {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        try {
            this.httpServletRequest.setCharacterEncoding(getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RequestException("Encoding " + getEncoding() + " is not supported", e);
        }
        this.parameters = new HashMap<String, String[]>(httpServletRequest.getParameterMap());
        try {
            uri = new URL(this.httpServletRequest.getRequestURL().toString());
        } catch (MalformedURLException e) {
            throw new RequestException("Can't create request context: malformed request uri " + this.httpServletRequest.getRequestURI());
        }
        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            this.sessionContext = (SessionContext)httpSession.getAttribute(SessionContext.SESSION_CONTEXT_PARAMETER_NAME);
            if (this.sessionContext != null) {
                this.processContext = sessionContext.getProcessContextIfExists(getParameterValue(ProcessContext.PARAMETER_NAME_ID));
            }
        }
        INSTANCE.set(this);
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getParameterValue(String name) {
        String[] values = parameters.get(name);
        if (values != null && values.length > 0) {
            return values[0];
        } else {
            return null;
        }
    }

    public void setParameterValue(String name, String value) {
        parameters.put(name, new String[]{value});
    }

    public String[] getParameterValues(String name) {
        String[] values = parameters.get(name);
        if (values == null) {
            values = new String[0];
        }
        return values;
    }

    public ProcessContext getProcessContext() {
        if (processContext == null) {
            processContext = getSessionContext().getProcessContext();
        }
        return processContext;
    }

    public ProcessContext getProcessContextIfExists() {
        return processContext;
    }

    public SessionContext getSessionContext() {
        if (sessionContext == null) {
            sessionContext = createSessionContext();
            httpServletRequest.getSession().setAttribute(SessionContext.SESSION_CONTEXT_PARAMETER_NAME, sessionContext);
        }
        return sessionContext;
    }

    public SessionContext getSessionContextIfExists() {
        return sessionContext;
    }

    protected SessionContext createSessionContext() {
        return new SessionContext();
    }

    public ApplicationContext getApplicationContext() {
return null;//todo: implement this
    }

    public StringBuilder getResponse() {
        return response;
    }

    public Document getRequestDocument() {
        return requestDocument;
    }

    protected Document resolveRequestDocument() throws MalformedURLException, RequestException {
        return getDocument(new URL(getHttpServletRequest().getRequestURL().toString()).getPath());
    }

    protected void setRequestDocument(Document requestDocument) {
        this.requestDocument = requestDocument;
    }

    public Document getResponseDocument() {
        return responseDocument;
    }

    public void setResponseDocument(Document responseDocument) {
        setResponseDocument(responseDocument, responseDocument.getResponseType());
    }

    public void setResponseDocument(Document responseDocument, Engine.ResponseType responseType) {
        this.responseDocument = responseDocument;
        this.responseType = responseType;
    }

    public ActionComponent getEventSource() {
        return eventSource;
    }

    public void setEventSource(ActionComponent eventSource) {
        this.eventSource = eventSource;
    }

    public boolean isInSourceEventForm() {
        return inSourceEventForm;
    }

    public void setInSourceEventForm(boolean inSourceEventForm) {
        this.inSourceEventForm = inSourceEventForm;
    }

    public URL getURI() {
        return uri;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Engine.Mode getMode() {
        return mode;
    }

    public void setMode(Engine.Mode mode) {
        this.mode = mode;
    }

    public Engine.ResponseType getResponseType() {
        return responseType;
    }

    public void release() throws ReleaseException {
        if (requestDocument != null) {
            requestDocument.release();
        }
        if (responseDocument != null && responseDocument != requestDocument) {
            responseDocument.release();
        }
        INSTANCE.set(null);
    }

    public abstract Document getDocument(String path) throws RequestException;

    public static RequestContext getRequestContext() {
        return INSTANCE.get();
    }

}
