package org.cooze.stepcounter.record;


import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.record.event.ContainerEvent;
import org.cooze.stepcounter.record.exception.SchemaDataException;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-06
 **/
public class SchemaDataContainerEvent implements ContainerEvent {

    private SchemaDataContainer schemaDataContainerA;
    private SchemaDataContainer schemaDataContainerB;

    public SchemaDataContainerEvent(SchemaDataContainer schemaDataContainerA, SchemaDataContainer schemaDataContainerB) throws SchemaDataException {
        this.schemaDataContainerA = schemaDataContainerA;
        this.schemaDataContainerB = schemaDataContainerB;
        if (this.schemaDataContainerA == null || this.schemaDataContainerB == null) {
            throw new SchemaDataException("schema container is null.");
        }
    }


    @Override
    public void save(TransportSchema schemaData) {
        if (this.schemaDataContainerA.currentStatus().compareTo(ContainerStatus.save) == 0) {
            this.schemaDataContainerA.getContainer().add(schemaData);
        } else if (this.schemaDataContainerB.currentStatus().compareTo(ContainerStatus.save) == 0) {
            this.schemaDataContainerB.getContainer().add(schemaData);
        }
    }

    @Override
    public SchemaDataContainer countSchema() {

        if (this.schemaDataContainerA.currentStatus().compareTo(ContainerStatus.save) == 0) {
            this.schemaDataContainerA.switchStatus(ContainerStatus.count);
            this.schemaDataContainerB.switchStatus(ContainerStatus.save);
            return this.schemaDataContainerA;
        }

        if (this.schemaDataContainerB.currentStatus().compareTo(ContainerStatus.save) == 0) {
            this.schemaDataContainerA.switchStatus(ContainerStatus.save);
            this.schemaDataContainerB.switchStatus(ContainerStatus.count);
            return this.schemaDataContainerB;
        }
        return null;
    }

    @Override
    public void clearCurrentContainer() {
        if (this.schemaDataContainerA.currentStatus().compareTo(ContainerStatus.count) == 0) {
            this.schemaDataContainerA.clear();
        }else if (this.schemaDataContainerB.currentStatus().compareTo(ContainerStatus.count) == 0) {
            this.schemaDataContainerB.clear();
        }
    }
}
