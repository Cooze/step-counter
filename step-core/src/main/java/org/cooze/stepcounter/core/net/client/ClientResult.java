package org.cooze.stepcounter.core.net.client;


import org.cooze.stepcounter.core.net.Result;
import org.cooze.stepcounter.core.protocol.Message;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public class ClientResult implements Result {

    private Message message;


    @Override
    public synchronized Message get(long timout) throws Exception {
        wait(timout);
        return this.message;
    }

    @Override
    public synchronized void set(Message message) {
        notifyAll();
        this.message = message;
    }
}
