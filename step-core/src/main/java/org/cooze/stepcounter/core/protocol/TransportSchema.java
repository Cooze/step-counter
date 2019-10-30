package org.cooze.stepcounter.core.protocol;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-07-29
 **/

public class TransportSchema implements Serializable {

    private TransportHead head;

    private TransportBody body;

    public TransportSchema(TransportHead head, TransportBody body) {
        this.head = head;
        this.body = body;
    }

    public TransportSchema() {
    }

    public TransportHead getHead() {
        return head;
    }

    public void setHead(TransportHead head) {
        this.head = head;
    }

    public TransportBody getBody() {
        return body;
    }

    public void setBody(TransportBody body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransportSchema)) return false;
        TransportSchema that = (TransportSchema) o;
        return Objects.equals(head, that.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head);
    }

    @Override
    public String toString() {
        return "TransportSchema{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }
}
