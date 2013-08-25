package ru.albemuth.frontend;

import org.apache.log4j.Logger;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.components.RedirectDocument;
import ru.albemuth.frontend.context.RequestContext;
import ru.albemuth.frontend.context.ProcessContext;
import ru.albemuth.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.OutputStream;
import java.net.MalformedURLException;

public class Engine extends HttpServlet implements Configured, Closed {

    private static final Logger LOG                         = Logger.getLogger(Engine.class);

    public enum Mode {
        SET_CONTEXT_PROPERTIES, PROCESS_EVENTS, RENDER, RELEASE
    }

    public enum ResponseType {
        FORWARD {
            public void processResponse(Engine engine, RequestContext requestContext, Document responseDocument)  throws IOException, RenderException {
                responseDocument.render();
                engine.renderResponse(requestContext.getHttpServletRequest(), requestContext.getHttpServletResponse(), requestContext.getResponse(), responseDocument.getContentType(), requestContext.getEncoding());
            }},
        REDIRECT {
            public void processResponse(Engine engine, RequestContext requestContext, Document responseDocument) throws IOException, RenderException {
                String redirectPath = responseDocument.getPath();
                if (!redirectPath.startsWith("http://")) {
                    redirectPath = requestContext.getHttpServletRequest().getContextPath() + redirectPath + (requestContext.getProcessContextIfExists() != null ? "?" + ProcessContext.PARAMETER_NAME_ID  + "=" + requestContext.getProcessContextIfExists().getProcessid() : "");
                }
                requestContext.getHttpServletResponse().sendRedirect(redirectPath);
            }};

        public abstract void processResponse(Engine engine, RequestContext requestContext, Document responseDocument)  throws IOException, RenderException;
    }

    private DocumentPool documentPool;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        try {
            configure(getConfiguration());
        } catch (IOException e) {
            LOG.error("Can't get configuration instance", e);
        } catch (ConfigurationException e) {
            LOG.error("Can't configure frontend engine", e);
        }
    }

    public void destroy() {
        try {
            close();
        } catch (CloseException e) {
            LOG.error("Can't close Engine", e);
        }
        super.destroy();
    }

    protected Configuration getConfiguration() throws IOException {
        return new Configuration("/frontend.properties");
    }

    public void configure(Configuration cfg) throws ConfigurationException {
        this.documentPool = createDocumentPoolInstance(cfg);
        this.documentPool.configure(cfg);
    }

    public void close() throws CloseException {
        this.documentPool.close();
    }

    protected DocumentPool createDocumentPoolInstance(Configuration cfg) throws ConfigurationException {
        return Configuration.createInstance(DocumentPool.class, cfg.getStringValue(this, "document-pool-class", DocumentPool.class.getName()));
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Document requestDocument, responseDocument;
        RequestContext requestContext = null;
        try {
            requestContext = createRequestContext(request, response);
            requestDocument = requestContext.getRequestDocument();
            requestDocument.setContextProperties();
            requestDocument.processEvents();
            responseDocument = requestContext.getResponseDocument();
            requestContext.getResponseType().processResponse(this, requestContext, responseDocument);

            /*if (RequestContext.ResponseType.FORWARD.equals(requestContext.getResponseType())) {
                responseDocument.render();
                renderResponse(request, response, requestContext.getResponse(), responseDocument.getContentType(), requestContext.getEncoding());
            } else if (RequestContext.ResponseType.REDIRECT.equals(requestContext.getResponseType())) {
                response.sendRedirect(request.getContextPath() + responseDocument.getPath() + (requestContext.getProcessContextIfExists() != null ? "?" + ProcessContext.PARAMETER_NAME_ID  + "=" + requestContext.getProcessContextIfExists().getProcessid() : ""));
            } else {
                throw new RenderException("Unknown request context response type: " + requestContext.getResponseType());
            }*/
        } catch (RequestException e) {
            logFrontendException(e);
            handleFrontendException(request, response, requestContext, e, "text/html", requestContext != null ? requestContext.getEncoding() : "Windows-1251");
        } catch (EventException e) {
            logFrontendException(e);
            handleFrontendException(request, response, requestContext, e, "text/html", requestContext.getEncoding());
        } catch (RenderException e) {
            logFrontendException(e);
            handleFrontendException(request, response, requestContext, e, "text/html", requestContext.getEncoding());
        } catch(Exception e) {
            logException("Unexpected exception", e);
            renderExceptionResponse(request, response, e, "text/html", requestContext != null ? requestContext.getEncoding() : "Windows-1251");
        } finally {
            try {
                if (requestContext != null) {
                    requestContext.release();
                }
            } catch (ReleaseException e) {
                LOG.error("ReleaseException in request context release", e);
            }
        }
    }

    protected RequestContext createRequestContext(HttpServletRequest request, HttpServletResponse response) throws RequestException {
        return new FrontendRequestContext(request, response);
    }

    protected void logFrontendException(RequestException e) {
        logFrontendException("RequestException", e);
    }

    protected void logFrontendException(EventException e) {
        logFrontendException("EventException", e);
    }

    protected void logFrontendException(RenderException e) {
        logFrontendException("RenderException", e);
    }

    protected void logFrontendException(ReleaseException e) {
        logFrontendException("ReleaseException", e);
    }

    protected void logFrontendException(String message, FrontendException e) {
        e.log(message, LOG);
    }

    protected void logException(String message, Exception e) {
        LOG.error(message, e);
    }

    protected void handleFrontendException(HttpServletRequest request, HttpServletResponse response, RequestContext requestContext, FrontendException e, String contentType, String encoding) throws IOException {
        if (requestContext != null && e.getDocument() != null) {
            try {
                (e.getDocument().getResponseType() != null ? e.getDocument().getResponseType() : ResponseType.FORWARD).processResponse(this, requestContext, e.getDocument());
                e.getDocument().release();
                documentPool.returnDocumentToPool(e.getDocument().getPath(), e.getDocument());
            } catch (RenderException re) {
                LOG.error("RenderException while handle frontend exception", re);
                handleFrontendException(request, response, requestContext, re, contentType, encoding);
            } catch (ReleaseException re) {
                LOG.error("ReleaseException while handle frontend exception", re);
                handleFrontendException(request, response, requestContext, re, contentType, encoding);
            }
        } else if (e.getRedirectPath() != null) {
            response.sendRedirect(request.getContextPath() + e.getRedirectPath());
        } else {
            renderExceptionResponse(request, response, e, contentType, encoding);
        }
    }

    protected void renderResponse(HttpServletRequest request, HttpServletResponse response, CharSequence responseContent, String contentType, String encoding) throws IOException {
        response.setCharacterEncoding(encoding);
        response.setHeader("Content-type", contentType);
        OutputStream out = response.getOutputStream();
        out.write(responseContent.toString().getBytes(encoding));
        /*PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), encoding));
        out.println(responseContent);*/
        out.close();
    }

    protected void renderExceptionResponse(HttpServletRequest request, HttpServletResponse response, Exception e, String contentType, String encoding) throws IOException {
        response.setCharacterEncoding(encoding);
        response.setHeader("Content-type", contentType);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), encoding));
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Ошибочка вышла...</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("В процессе обработки запроса произошла непредвиденная ошибка:");
        out.println("<pre>");
        e.printStackTrace(out);
        out.println("</pre>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    protected class FrontendRequestContext extends RequestContext {

        protected FrontendRequestContext(HttpServletRequest request, HttpServletResponse response) throws RequestException {
            super(request, response);
            try {
                setRequestDocument(resolveRequestDocument());
            } catch (MalformedURLException e) {
                throw new RequestException("Can't extract path from http servlet request to " + request.getRequestURL(), e);
            }
        }

        public Document getDocument(String path) throws RequestException {
            Document document = documentPool.getDocumentFromPool(path);
            document.setRequestContext(this);
            return document;
        }

        public void release() throws ReleaseException {
            super.release();
            Document requestDocument = getRequestDocument();
            Document responseDocument = getResponseDocument();
            if (requestDocument != null) {
                documentPool.returnDocumentToPool(requestDocument.getPath(), requestDocument);
            }
            if (responseDocument != null && responseDocument != requestDocument && !(responseDocument instanceof RedirectDocument)) {
                documentPool.returnDocumentToPool(responseDocument.getPath(), responseDocument);
            }
        }
    }

}
