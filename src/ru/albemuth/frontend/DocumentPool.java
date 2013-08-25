package ru.albemuth.frontend;

import ru.albemuth.frontend.components.Document;
import ru.albemuth.frontend.metadata.ComponentClassBuilder;
import ru.albemuth.frontend.metadata.ComponentClassBuilderJavassistImpl;
import ru.albemuth.frontend.metadata.ComponentParser;
import ru.albemuth.frontend.metadata.MetadataException;
import ru.albemuth.util.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DocumentPool implements Configured, Closed {

    private Map<String, Pool<Document>> pools;
    private int minPoolSize;
    private int maxPoolSize;
    private int whenExhaustedAction;
    private long maxWait;
    private ComponentParser componentParser;

    public void configure(Configuration cfg) throws ConfigurationException {
        this.pools = new ConcurrentHashMap<String, Pool<Document>>();
        this.minPoolSize = cfg.getIntValue(this, "min-pool-size", 1);
        this.maxPoolSize = cfg.getIntValue(this, "max-pool-size", 10);
        this.whenExhaustedAction = cfg.getIntValue(this, "when-exhausted-action", Pool.GROW_WHEN_EXHAUSTED);
        this.maxWait = cfg.getLongValue(this, "max-wait", 10000);
        //ComponentClassBuilder componentClassBuilder = Configuration.createInstance(ComponentClassBuilder.class, cfg.getStringValue(this, "component-class-builder-class", ComponentClassBuilderJavassistImpl.class.getName()));
        ComponentClassBuilder componentClassBuilder = new ComponentClassBuilderJavassistImpl("jaccb");
        componentParser = new ComponentParser(componentClassBuilder);
        componentParser.configure(cfg);
        Accessor.getAccessor(DocumentPool.class).setDefaultInstance(this);
    }

    public void close() throws CloseException {}

    public Document getDocumentFromPool(String path) throws RequestException {
        try {
            return getPool(path).getObject();
        } catch (PoolException e) {
            throw new RequestException("Can't obtain document from pool for path " + path, e);
        }
    }

    public void returnDocumentToPool(String path, Document document) throws ReleaseException {
        try {
            getPool(path).returnObject(document);
        } catch (PoolException e) {
            throw new ReleaseException("Can't return document to pool for path " + path, e);
        }
    }

    private Pool<Document> getPool(String path) throws PoolException {
        Pool<Document> pool = pools.get(path);
        if (pool == null) {
            synchronized (this) {
                pool = pools.get(path);
                if (pool == null) {
                    pool = createPool(path);
                    pools.put(path, pool);
                }
            }
        }
        return pool;
    }

    private Pool<Document> createPool(String path) throws PoolException {
        return new Pool<Document>(path, minPoolSize, maxPoolSize, whenExhaustedAction, maxWait, new DocumentFactory(path));
    }

    public String getDocumentName(String path) {
        return path;
    }

    class DocumentFactory implements PoolItemFactory<Document> {

        private String path;
        private Class<Document> documentClass;

        @SuppressWarnings("unchecked")
        public DocumentFactory(String path) throws PoolException {
            this.path = path;
            try {
                this.documentClass = (Class<Document>)componentParser.getDocumentDescriptor(getDocumentName(path)).createComponent(null).getClass();
            } catch (MetadataException e) {
                throw new PoolException("Can't resolve document class for path " + path, e);
            }
        }

        public Document getNewPoolItem() throws PoolException {
            try {
                Document document = (Document)componentParser.getDocumentDescriptor(getDocumentName(path)).createComponent(null);
                document.setPath(path);
                return document;
            } catch (MetadataException e) {
                throw new PoolException("Can't create new document instance for path " + path, e);
            }
        }

        public Class<Document> getPoolItemClass() throws PoolException {
            return documentClass;
        }

    }

}
