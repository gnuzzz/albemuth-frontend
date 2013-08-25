package ru.albemuth.frontend.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 25.10.2007
 * Time: 1:02:14
 */
public class SessionContext extends Context<Object> {

    public static final String SESSION_CONTEXT_PARAMETER_NAME                   = "session-context";

    private Map<String, Object> values = new ConcurrentHashMap<String, Object>();
    private Map<String, ProcessContext> processes = new ConcurrentHashMap<String, ProcessContext>();

    public Object getParameterValue(String name) {
        return values.get(name);
    }

    public void setParameterValue(String name, Object value) {
        if (value == null) {
            values.remove(name);
        } else {
            values.put(name, value);
        }
    }

    public ProcessContext getProcessContext() {
        ProcessContext processContext = new ProcessContext();
        processes.put(processContext.getProcessid(), processContext);
        return processContext;
    }

    public ProcessContext getProcessContext(String processid) {
        ProcessContext processContext = processes.get(processid);;
        if (processContext == null) {
            processContext = getProcessContext();
        }
        return processContext;
    }

    public ProcessContext getProcessContextIfExists(String processid) {
        return processid != null ? processes.get(processid) : null;
    }

}
