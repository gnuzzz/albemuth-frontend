package ru.albemuth.frontend.samples.helloworld;

import ru.albemuth.frontend.components.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class HelloWorld extends Document {

    private DateFormat df = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

    public HelloWorld(String name) {
        super(name);
    }

    public String getCurrentDate() {
        return df.format(new Date());
    }
    
}
