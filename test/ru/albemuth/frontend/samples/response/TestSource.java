package ru.albemuth.frontend.samples.response;

import junit.framework.TestCase;
import ru.albemuth.frontend.samples.SamplesEngine;
import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.test.HTTPServletResponse;

public class TestSource extends TestCase {

    public void test() {
        try {
            SamplesEngine engine = new SamplesEngine();
            engine.init(null);
            HTTPServletRequest request;
            HTTPServletResponse response;
            request = new HTTPServletRequest();
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/response/Source");
            response = new HTTPServletResponse();
            engine.service(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
