package org.cooze.stepcounter.core.protocol;


import java.io.Serializable;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/

public enum MessageType implements Serializable {
    PING, PONG, PAYLOAD
}
