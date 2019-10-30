package org.cooze.stepcounter.record;

import org.cooze.stepcounter.core.protocol.TransportSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-06
 **/
public class SchemaDataContainer {

    private ContainerStatus status;
    private List<TransportSchema> container;

    public SchemaDataContainer(ContainerStatus status) {
        this.container = new ArrayList<>();
        this.status = status;
    }

    public void clear() {
        if (this.container == null) {
            return;
        }
        this.container.clear();
    }

    public ContainerStatus currentStatus() {
        return this.status;
    }

    public void switchStatus(ContainerStatus status) {
        this.status = status;
    }

    public List<TransportSchema> getContainer() {
        return this.container;
    }


}
