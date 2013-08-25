package ru.albemuth.frontend.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 25.10.2007
 * Time: 1:01:35
 */
public class ProcessContext extends Context<Object> {

    public static final String PARAMETER_NAME_ID = "processid";

    private String processid = "" + System.currentTimeMillis();
    private Map<String, Object> values = new ConcurrentHashMap<String, Object>();

    public String getProcessid() {
        return processid;
    }

    public Object getParameterValue(String name) {
        return values.get(name);
    }

    public void setParameterValue(String name, Object value) {
        values.put(name, value);
    }

}
