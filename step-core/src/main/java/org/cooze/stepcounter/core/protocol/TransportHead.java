package org.cooze.stepcounter.core.protocol;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/

public class TransportHead implements Serializable {
    private String schemaName;
    private String timeFrame;
    private long timestamp;

    public TransportHead(String schemaName) {
        this.schemaName = schemaName;
    }

    public TransportHead() {
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(String timeFrame) {
        this.timeFrame = timeFrame;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransportHead)) return false;
        TransportHead that = (TransportHead) o;
        return Objects.equals(schemaName, that.schemaName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName);
    }

    @Override
    public String toString() {
        return "TransportHead{" +
                "schemaName='" + schemaName + '\'' +
                ", timeFrame='" + timeFrame + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
