package ru.albemuth.frontend;

import junit.framework.TestCase;
import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.test.HTTPServletResponse;
import ru.albemuth.frontend.test.HTTPServletRequest;

import javax.servlet.http.HttpSession;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 01.02.2008
 * Time: 1:46:25
 */
public class TestFrontend extends TestCase {

    public void testStub() {}

    public HTTPServletRequest createHttpServletRequest(Document requestDocument) {
        return new HTTPServletRequest(requestDocument);
    }

    public HTTPServletRequest createHttpServletRequest(HttpSession httpSession) {
        return new HTTPServletRequest(httpSession);
    }

    public HTTPServletRequest createHttpServletRequest() {
        return new HTTPServletRequest();
    }

    public HTTPServletResponse createHttpServletResponse() {
        return new HTTPServletResponse();
    }

}
