package org.cooze.stepcounter.receiver.handler;


import org.cooze.stepcounter.core.net.server.MessageHandler;
import org.cooze.stepcounter.core.protocol.TransportSchema;
import org.cooze.stepcounter.core.utils.StringUtils;
import org.cooze.stepcounter.receiver.DataReceiver;

import javax.annotation.Resource;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-14
 **/
public class MessageHandlerImpl implements MessageHandler {

    @Resource
    private DataReceiver dataReciever;

    @Override
    public void handler(TransportSchema schema) {
        if (schema == null || StringUtils.isEmpty(schema.getHead().getSchemaName())) {
            return;
        }
        dataReciever.putData(schema.getHead().getSchemaName(), schema);
    }
}
