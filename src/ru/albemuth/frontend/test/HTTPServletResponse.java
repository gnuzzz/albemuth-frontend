package ru.albemuth.frontend.test;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class HTTPServletResponse implements HttpServletResponse {

    private ByteArrayOutputStream out = new ByteArrayOutputStream();
    private Collection<Cookie> responseCookies = new ArrayList<Cookie>();
    private Map<String, String> responseHeaders = new HashMap<String, String>();
    private int responseCode;
    private String responseMessage;
    private String redirectPath;

    public void addCookie(Cookie cookie) {
        responseCookies.add(cookie);
    }

    public Collection<Cookie> getResponseCookies() {
        return responseCookies;
    }

    public boolean containsHeader(String s) {
        return responseHeaders.get(s) != null;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public String encodeURL(String s) {
        return null;
    }

    public String encodeRedirectURL(String s) {
        return null;
    }

    @Deprecated
    public String encodeUrl(String s) {
        return null;
    }

    @Deprecated
    public String encodeRedirectUrl(String s) {
        return null;
    }

    public void sendError(int i, String s) throws IOException {
        responseCode = i;
        responseMessage = s;
    }

    public void sendError(int i) throws IOException {
        responseCode = i;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void sendRedirect(String s) throws IOException {
        redirectPath = s;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void setDateHeader(String s, long l) {

    }

    public void addDateHeader(String s, long l) {

    }

    public void setHeader(String s, String s1) {
        responseHeaders.put(s, s1);
    }

    public void addHeader(String s, String s1) {

    }

    public void setIntHeader(String s, int i) {

    }

    public void addIntHeader(String s, int i) {

    }

    public void setStatus(int i) {
        responseCode = i;
    }

    @Deprecated
    public void setStatus(int i, String s) {

    }

    public String getCharacterEncoding() {
        return "Cp1251";
    }

    public String getContentType() {
        return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new ServletOutputStream() {
            public void write(int i) throws IOException {
                out.write(i);
            }
        };
    }

    public PrintWriter getWriter() throws IOException {
        return null;
    }

    public void setCharacterEncoding(String s) {

    }

    public void setContentLength(int i) {

    }

    public void setContentType(String s) {

    }

    public void setBufferSize(int i) {

    }

    public int getBufferSize() {
        return 0;
    }

    public void flushBuffer() throws IOException {

    }

    public void resetBuffer() {

    }

    public boolean isCommitted() {
        return false;
    }

    public void reset() {

    }

    public void setLocale(Locale locale) {

    }

    public Locale getLocale() {
        return null;
    }

    public String getContent() {
        try {
            return out.toString(getCharacterEncoding());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    @Override
    public int getStatus() {
        return 0;//todo: implementh this
    }

    @Override
    public String getHeader(String s) {
        return null;//todo: implementh this
    }

    @Override
    public Collection<String> getHeaders(String s) {
        return null;//todo: implementh this
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;//todo: implementh this
    }*/
}
