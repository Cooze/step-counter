package org.cooze.stepcounter.core.protocol;

import java.io.Serializable;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public class Message implements Serializable {

    private MessageType type;
    private TransportSchema payload;

    public Message(MessageType type, TransportSchema payload) {
        this.type = type;
        this.payload = payload;
    }

    public Message(TransportSchema payload) {
        this.type = MessageType.PAYLOAD;
        this.payload = payload;
    }

    public Message(MessageType type) {
        this.type = type;
    }

    public Message() {
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public TransportSchema getPayload() {
        return payload;
    }

    public void setPayload(TransportSchema payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", payload=" + payload +
                '}';
    }
}
