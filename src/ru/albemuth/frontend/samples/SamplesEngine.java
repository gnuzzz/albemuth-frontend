package ru.albemuth.frontend.samples;

import ru.albemuth.frontend.Engine;
import ru.albemuth.frontend.samples.inquiry.InquiryController;
import ru.albemuth.util.Configuration;
import ru.albemuth.util.ConfigurationException;

public class SamplesEngine extends Engine {

    public void configure(Configuration cfg) throws ConfigurationException {
        super.configure(cfg);
        InquiryController inquiryController = new InquiryController();
        inquiryController.configure(cfg);
    }
    
}
