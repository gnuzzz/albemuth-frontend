package ru.albemuth.frontend.samples.inquiry;

import ru.albemuth.frontend.test.HTTPServletRequest;
import ru.albemuth.frontend.test.HTTPServletResponse;
import ru.albemuth.frontend.TestFrontend;
import ru.albemuth.frontend.samples.SamplesEngine;

public class TestInquiry extends TestFrontend {

    public void test() {
        try {
            SamplesEngine engine = new SamplesEngine();
            engine.init(null);
            HTTPServletRequest request;
            HTTPServletResponse response;
            request = createHttpServletRequest();
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/inquiry/InquirySample");
            response = createHttpServletResponse();
            engine.service(request, response);

            request = createHttpServletRequest(request.getSession(false));
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/inquiry/InquirySample");
            request.mockSetParameter("loginForm", "");
            request.mockSetParameter("login", "aaa");
            request.mockSetParameter("register", "Зарегистрироваться");
            response = createHttpServletResponse();
            engine.service(request, response);

            request = createHttpServletRequest(request.getSession(false));
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/inquiry/InquirySample");
            request.mockSetParameter("inquiryForm", "");
            request.mockSetParameter("choices-select.Укажите ЖЖЖ", "ББ");
            request.mockSetParameter("choices-select.Укажите 000", "2222");
            request.mockSetParameter("choice-radio", ".Y");
            request.mockSetParameter("choice-checkbox.@", "");
            request.mockSetParameter("answer", "Жми!");
            response = createHttpServletResponse();
            engine.service(request, response);

            request = createHttpServletRequest(request.getSession(false));
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/inquiry/InquirySample?showResults=off ");
            response = createHttpServletResponse();
            engine.service(request, response);
            System.out.println(response.getContent());


            request = createHttpServletRequest(request.getSession(false));
            request.mockSetRequestURI("http://localhost/ru/albemuth/frontend/samples/inquiry/InquirySample");
            request.mockSetParameter("inquiryForm", "");
            request.mockSetParameter("choices-select.Укажите ЖЖЖ", "АА");
            request.mockSetParameter("choices-select.Укажите 000", "3333");
            request.mockSetParameter("choice-radio", ".X");
            request.mockSetParameter("choice-checkbox.$", "");
            request.mockSetParameter("answer", "Жми!");
            response = createHttpServletResponse();
            engine.service(request, response);
//System.out.println(response.getContent());

            engine.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
