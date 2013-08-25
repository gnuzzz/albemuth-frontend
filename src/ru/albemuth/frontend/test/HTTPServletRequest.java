package ru.albemuth.frontend.test;

import ru.albemuth.frontend.components.Document;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 01.02.2008
 * Time: 1:50:20
 */
public class HTTPServletRequest implements HttpServletRequest {

    private String authType;
    private Cookie[] cookies;
    private Map<String, List<String>> headersMap = new HashMap<String, List<String>>();
    private String method;
    private String pathInfo;
    private String pathTranslated;
    private String contextPath = "";
    private String remoteUser;
    private Principal userPrincipal;
    private String requestURI;
    private Map<String, String[]> parameterMap = new HashMap<String, String[]>();
    private String servletPath;
    private HttpSession session;
    private String characterEncoding = "Cp1251";
    private int contentLength;
    private String contentType;

    public HTTPServletRequest() {}

    public HTTPServletRequest(Document requestDocument) {
        mockSetRequestURI("http://localhost/" + requestDocument.getName());
    }

    public HTTPServletRequest(HttpSession httpSession) {
        this.session = httpSession;
    }

    public String getAuthType() {
        return authType;
    }

    public void mockSetAuthType(String authType) {
        this.authType = authType;
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void mockSetCookies(Cookie[] cookies) {
        this.cookies = cookies;
    }

    public long getDateHeader(String s) {
        String header = getHeader(s);
        if (header != null) {
            try {
                return Long.parseLong(header);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void mockSetDateHeader(String name, long value) {
        mockSetHeader(name, "" + value);
    }

    public String getHeader(String s) {
        List<String> headers = headersMap.get(s);
        return headers != null && headers.size() > 0 ? headers.get(0) : null;
    }

    public void mockSetHeader(String name, String value) {
        List<String> headers = headersMap.get(name);
        if (headers == null) {
            headers = new ArrayList<String>();
            headersMap.put(name, headers);
        }
        headers.add(value);
    }

    public Enumeration getHeaders(String s) {
        List<String> headers = headersMap.get(s);
        if (headers == null) {
            headers = Collections.EMPTY_LIST;
        }
        return Collections.enumeration(headers);
    }

    public Enumeration getHeaderNames() {
        return Collections.enumeration(headersMap.keySet());
    }

    public int getIntHeader(String s) {
        String header = getHeader(s);
        if (header != null) {
            try {
                return Integer.parseInt(header);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void mockSetIntHeader(String name, int value) {
        mockSetHeader(name, "" + value);
    }

    public String getMethod() {
        return method;
    }

    public void mockSetMethod(String method) {
        this.method = method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public void mockSetPathInfo(String pathInfo) {
        this.pathInfo = pathInfo;
    }

    public String getPathTranslated() {
        return pathTranslated;
    }

    public void mockSetPathTranslated(String pathTranslated) {
        this.pathTranslated = pathTranslated;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void mockSetContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getQueryString() {
        try {
            return new URL(getRequestURI()).getQuery();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public String getRemoteUser() {
        return remoteUser;
    }

    public void mockSetRemoteUser(String remoteUser) {
        this.remoteUser = remoteUser;
    }

    public boolean isUserInRole(String s) {
        return false;//todo: implement this
    }

    public Principal getUserPrincipal() {
        return userPrincipal;
    }

    public void mockSetUserPrincipal(Principal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    public String getRequestedSessionId() {
        return null;//todo: implement this
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void mockSetRequestURI(String requestURI) {
        this.requestURI = requestURI;
        this.parameterMap = new HashMap<String, String[]>();
        if (requestURI != null && requestURI.indexOf('?') != -1) {
            String query = requestURI.substring(requestURI.indexOf('?') + 1);
            for (String parameter: query.split("&")) {
                if (parameter.indexOf('=') != -1) {
                    this.parameterMap.put(parameter.substring(0, parameter.indexOf('=')), new String[]{parameter.substring(parameter.indexOf('=') + 1)});
                } else {
                    this.parameterMap.put(parameter, new String[]{""});
                }
            }
        }
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(requestURI);
    }

    public String getServletPath() {
        return servletPath;
    }

    public void mockSetServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public HttpSession getSession(boolean b) {
        if (session == null && b) {
            session = new HttpSession() {

                private Map<String, Object> attributes = new HashMap<String, Object>();

                public long getCreationTime() {
                    return 0;
                }

                public String getId() {
                    return null;
                }

                public long getLastAccessedTime() {
                    return 0;
                }

                public ServletContext getServletContext() {
                    return null;
                }

                public void setMaxInactiveInterval(int i) {
                }

                public int getMaxInactiveInterval() {
                    return 0;
                }

                @Deprecated
                public HttpSessionContext getSessionContext() {
                    return null;
                }

                public Object getAttribute(String s) {
                    return attributes.get(s);
                }

                public Object getValue(String s) {
                    return attributes.get(s);
                }

                public Enumeration getAttributeNames() {
                    return null;
                }

                public String[] getValueNames() {
                    return attributes.keySet().toArray(new String[0]);
                }

                public void setAttribute(String s, Object o) {
                    attributes.put(s, o);
                }

                public void putValue(String s, Object o) {
                    attributes.put(s, o);
                }

                public void removeAttribute(String s) {
                    attributes.remove(s);
                }

                public void removeValue(String s) {
                    attributes.remove(s);
                }

                public void invalidate() {
                }

                public boolean isNew() {
                    return false;
                }
            };
        }
        return session;
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public void mockSetSession(HttpSession session) {
        this.session = session;
    }

    public boolean isRequestedSessionIdValid() {
        return false;//todo: implement this
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;//todo: implement this
    }

    public boolean isRequestedSessionIdFromURL() {
        return false;//todo: implement this
    }

    public boolean isRequestedSessionIdFromUrl() {
        return false;//todo: implement this
    }

    public Object getAttribute(String s) {
        return null;//todo: implement this
    }

    public Enumeration getAttributeNames() {
        return null;//todo: implement this
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        this.characterEncoding = s;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void mockSetContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public void mockSetContentType(String contentType) {
        this.contentType = contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        return null;//todo: implement this
    }

    public String getParameter(String name) {
        String[] parameterValues = parameterMap.get(name);
        return parameterValues != null && parameterValues.length > 0 ? parameterValues[0] : null;
    }

    public void mockSetParameter(String name, String value) {
        String[] parameterValues = parameterMap.get(name);
        if (parameterValues != null) {
            String[] ps = new String[parameterValues.length + 1];
            System.arraycopy(parameterValues, 0, ps, 0, parameterValues.length);
            parameterValues = ps;
        } else {
            parameterValues = new String[1];
        }
        parameterMap.put(name, parameterValues);
        parameterValues[parameterValues.length - 1] = value;
    }

    public Enumeration getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    public String[] getParameterValues(String s) {
        List<String> parameterValues = new ArrayList<String>();
        for (String[] values: parameterMap.values()) {
            for (String value: values) {
                parameterValues.add(value);
            }
        }
        return parameterValues.toArray(new String[0]);
    }

    public Map getParameterMap() {
        return parameterMap;
    }

    public String getProtocol() {
        try {
            return new URL(getRequestURI()).getProtocol();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public String getScheme() {
        return null;//todo: implementh this
    }

    public String getServerName() {
        try {
            return new URL(getRequestURI()).getHost();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public int getServerPort() {
        try {
            return new URL(getRequestURI()).getPort();
        } catch (MalformedURLException e) {
            return 0;
        }
    }

    public BufferedReader getReader() throws IOException {
        return null;//todo: implementh this
    }

    public String getRemoteAddr() {
        return null;//todo: implementh this
    }

    public String getRemoteHost() {
        return null;//todo: implementh this
    }

    public void setAttribute(String s, Object o) {
        //todo: implementh this
    }

    public void removeAttribute(String s) {
        //todo: implementh this
    }

    public Locale getLocale() {
        return null;//todo: implementh this
    }

    public Enumeration getLocales() {
        return null;//todo: implementh this
    }

    public boolean isSecure() {
        return false;//todo: implementh this
    }

    public RequestDispatcher getRequestDispatcher(String s) {
        return null;//todo: implementh this
    }

    public String getRealPath(String s) {
        return null;//todo: implementh this
    }

    public int getRemotePort() {
        return 0;//todo: implementh this
    }

    public String getLocalName() {
        return null;//todo: implementh this
    }

    public String getLocalAddr() {
        return null;//todo: implementh this
    }

    public int getLocalPort() {
        return 0;//todo: implementh this
    }

    /*
    @Override
    public boolean authenticate(HttpServletResponse httpServletResponse) throws IOException, ServletException {
        return false;//todo: implementh this
    }

    @Override
    public void login(String s, String s1) throws ServletException {
        //todo: implementh this
    }

    @Override
    public void logout() throws ServletException {
        //todo: implementh this
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null; //todo: implementh this
    }

    @Override
    public Part getPart(String s) throws IOException, ServletException {
        return null;//todo: implementh this
    }

    @Override
    public ServletContext getServletContext() {
        return null;//todo: implementh this
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;//todo: implementh this
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;//todo: implementh this
    }

    @Override
    public boolean isAsyncStarted() {
        return false;//todo: implementh this
    }

    @Override
    public boolean isAsyncSupported() {
        return false;//todo: implementh this
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;//todo: implementh this
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;//todo: implementh this
    }*/
}
