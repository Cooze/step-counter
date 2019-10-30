package org.cooze.stepcounter.core;


import org.cooze.stepcounter.core.protocol.TransportSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-07
 **/
public class SchemaContainer {
    private SchemaContainer() {

    }

    private static class Holder {
        private final static SchemaContainer instance = new SchemaContainer();
        private static final Map<String, TransportSchema> schemaMap = new HashMap<>();
    }

    public static SchemaContainer instance() {
        return Holder.instance;
    }

    public void putSchema(TransportSchema schema) {
        Holder.schemaMap.put(schema.getHead().getSchemaName(), schema);
    }

    public TransportSchema getSchema(String schemaName) {
        return Holder.schemaMap.get(schemaName);
    }
}
