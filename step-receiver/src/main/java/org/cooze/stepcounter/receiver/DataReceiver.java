package org.cooze.stepcounter.receiver;


import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.receiver.utils.SchemaXmlUtils;
import org.cooze.stepcounter.record.SchemaDataContainerEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-07
 **/
public class DataReceiver {
    private static class Holder {
        private static final Map<String, SchemaDataContainerEvent> schemaContainerEvent = new ConcurrentHashMap<>();
    }

    private String nodeName;

    public DataReceiver() {
    }

    public void putData(String schema, TransportSchema data) {
        if (Holder.schemaContainerEvent.get(schema) == null) {
            return;
        }
        Holder.schemaContainerEvent.get(schema).save(data);
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public void init() {
        SchemaXmlUtils.readSchemaXmlFromClasspath(nodeName, Holder.schemaContainerEvent);
    }


}
