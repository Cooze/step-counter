package org.cooze.stepcounter.core.net.channel;

import io.netty.channel.ChannelHandlerContext;
import org.cooze.stepcounter.core.net.Result;
import org.cooze.stepcounter.core.protocol.Message;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public class MessageChannelClient extends MessageChannelAb {


    private Result requestResult;

    public MessageChannelClient(Result requestResult) {
        this.requestResult = requestResult;
    }

    @Override
    public void messageHandler(ChannelHandlerContext ctx, Message message) {
        requestResult.set(message);
    }


    @Override
    protected void pongHandler(Message pong) {
        super.pongHandler(pong);
        requestResult.set(pong);
    }

    @Override
    protected void allIdleHandler(ChannelHandlerContext ctx) {
        super.allIdleHandler(ctx);
        //client send ping
        this.sendPingToServer(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        //client send ping
        this.sendPingToServer(ctx);
    }

    @Override
    protected void readerIdleHandler(ChannelHandlerContext ctx) {
        super.readerIdleHandler(ctx);
    }

}
