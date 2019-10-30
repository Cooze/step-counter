package org.cooze.stepcounter.record.event;


import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.record.SchemaDataContainer;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-06
 **/
public interface ContainerEvent {

    void save(TransportSchema schemaData);

    SchemaDataContainer countSchema();

    void clearCurrentContainer();
}
