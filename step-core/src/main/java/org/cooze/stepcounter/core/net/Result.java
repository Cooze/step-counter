package org.cooze.stepcounter.core.net;


import org.cooze.stepcounter.core.protocol.Message;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public interface Result {
    Message get(long timout) throws Exception;

    void set(Message message);
}
