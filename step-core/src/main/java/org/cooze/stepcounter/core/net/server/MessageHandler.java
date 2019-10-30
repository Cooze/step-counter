package org.cooze.stepcounter.core.net.server;


import org.cooze.stepcounter.core.protocol.TransportSchema;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public interface MessageHandler {

    void handler(TransportSchema schema);
}
