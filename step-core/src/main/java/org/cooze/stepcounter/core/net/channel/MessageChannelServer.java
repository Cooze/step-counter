package org.cooze.stepcounter.core.net.channel;

import io.netty.channel.ChannelHandlerContext;
import org.cooze.stepcounter.core.net.server.MessageHandler;
import org.cooze.stepcounter.core.protocol.Message;


/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public class MessageChannelServer extends MessageChannelAb {


    private MessageHandler messageHandler;

    public MessageChannelServer(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void messageHandler(ChannelHandlerContext ctx, Message message) {
        messageHandler.handler(message.getPayload());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
    }
}
