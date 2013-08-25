package ru.albemuth.frontend.components;

import ru.albemuth.frontend.RequestException;
import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.RenderException;

/**
 * Created by IntelliJ IDEA.
 * User: -
 * Date: 26.12.2007
 * Time: 1:15:02
 */
public abstract class Condition extends Component<Document> {

    public Condition(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public abstract boolean isCondition();

    public boolean isNegate() {
        return false;
    }

    public void setContextProperties() throws RequestException {
        if ((isCondition() && !isNegate()) || (!isCondition() && isNegate())) {
            super.setContextProperties();
        }
    }

    public void processEvents() throws EventException {
        if ((isCondition() && !isNegate()) || (!isCondition() && isNegate())) {
            super.processEvents();
        }
    }

    public void render() throws RenderException {
        if ((isCondition() && !isNegate()) || (!isCondition() && isNegate())) {
            super.render();
        }
    }

    /*public void release(String itemid) throws ReleaseException {
        if ((isCondition() && !isNegate()) || (!isCondition() && isNegate())) {
            super.release(itemid);
        }
    }*/

}
