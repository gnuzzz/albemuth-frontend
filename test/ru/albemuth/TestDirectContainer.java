package ru.albemuth;

import junit.framework.TestCase;
import ru.albemuth.frontend.Engine;

public class TestDirectContainer extends TestCase {

    public void test() {
        try {
            ru.albemuth.frontend.Engine engine = new Engine() {
                protected void handleFrontendException(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, ru.albemuth.frontend.FrontendException e, String contentType, String encoding) throws java.io.IOException {
                    throw new RuntimeException(e);
                }
                protected void renderExceptionResponse(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Exception e, String contentType, String encoding) throws java.io.IOException {
                    throw new RuntimeException(e);
                }
            };
            engine.configure(new ru.albemuth.util.Configuration(new java.util.Properties()));
            ru.albemuth.frontend.test.HTTPServletRequest request;
            ru.albemuth.frontend.test.HTTPServletResponse response;

            //[2008-11-14 15:50:27,096] DEBUG ru.biblion.www.WWWEngine [none] [EB880DABB48B32282AE3296317386D3A] [new 2008-11-14 15:50:26.666] - GET /aaa/pages/Catalog {addToCart: , itemid: 484345, themeid: 0}
            request = new ru.albemuth.frontend.test.HTTPServletRequest();
            request.mockSetMethod("GET");
            request.mockSetRequestURI("http://localhost/ru/albemuth/DirectContainer");
            response = new ru.albemuth.frontend.test.HTTPServletResponse();
            engine.service(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
