package ru.albemuth.frontend.components;

import ru.albemuth.frontend.EventException;
import ru.albemuth.frontend.RenderException;
import ru.albemuth.frontend.RequestException;

import java.util.Collection;
import java.util.Iterator;

public abstract class Repetition extends Component<Document> {

    private int index;
    private String itemid;

    public Repetition(String name, Component parent, Document document) {
        super(name, parent, document);
    }

    public Collection<?> getItems() {
        return null;
    }

    public Object[] getArray() {
        return null;
    }

    public Iterator<?> getIterator() {
        return null;
    }

    public Object getItem() {
        return null;
    }

    public void setItem(Object item) {}

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getItemid() {
        Object item = getItem();
        return (itemid != null ? itemid + "." : "") + (item != null ? item.hashCode() : "");
    }

    public int getLimit() {
        return -1;
    }

    private final boolean isInBounds(final int i) {
        return getLimit() < 0 || i < getLimit();
    }

    public void setContextProperties() throws RequestException {
        Iterator it;
        itemid = getRequestContext().getItemid();
        if (getItems() != null) {
            it = getItems().iterator();
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.setContextProperties();
            }
        } else if (getArray() != null) {
            Object[] array = getArray();
            for (int i = 0; i < array.length && isInBounds(i); i++) {
                setItem(array[i]);
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.setContextProperties();
            }
        } else if ((it = getIterator()) != null) {
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.setContextProperties();
            }
        }
        getRequestContext().setItemid(itemid);
    }

    public void processEvents() throws EventException {
        Iterator it;
        itemid = getRequestContext().getItemid();
        if (getItems() != null) {
            it = getItems().iterator();
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.processEvents();
                if (getRequestContext().getResponseDocument() != null) {
                    break;
                }
            }
        } else if (getArray() != null) {
            Object[] array = getArray();
            for (int i = 0; i < array.length && isInBounds(i); i++) {
                setItem(array[i]);
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.processEvents();
                if (getRequestContext().getResponseDocument() != null) {
                    break;
                }
            }
        } else if ((it = getIterator()) != null) {
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.processEvents();
                if (getRequestContext().getResponseDocument() != null) {
                    break;
                }
             }
        }
        getRequestContext().setItemid(itemid);
    }

    public void render() throws RenderException {
        Iterator it;
        itemid = getRequestContext().getItemid();
        if (getItems() != null) {
            it = getItems().iterator();
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.render();
            }
        } else if (getArray() != null) {
            Object[] array = getArray();
            for (int i = 0; i < array.length && isInBounds(i); i++) {
                setItem(array[i]);
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.render();
            }
        } else if ((it = getIterator()) != null) {
            for (int i = 0; it.hasNext() && isInBounds(i); i++) {
                setItem(it.next());
                setIndex(i);
                getRequestContext().setItemid(getItemid());
                super.render();
            }
        }
        getRequestContext().setItemid(itemid);
    }

}
