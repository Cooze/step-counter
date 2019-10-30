package org.cooze.stepcounter.core.net.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.cooze.stepcounter.core.protocol.Message;
import org.cooze.stepcounter.core.protocol.MessageType;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public abstract class MessageChannelAb extends ChannelInboundHandlerAdapter {

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        MessageType type = message.getType();
        switch (type) {
            case PING:
                sendPongToClient(ctx);
                break;
            case PONG:
                pongHandler(message);
                break;
            case PAYLOAD:
                messageHandler(ctx, message);
                break;
            default:
                break;
        }

    }

    @Override
    public final void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent stateEvent = (IdleStateEvent) evt;

        switch (stateEvent.state()) {
            case ALL_IDLE:
                allIdleHandler(ctx);
                break;
            case READER_IDLE:
                readerIdleHandler(ctx);
                break;
            case WRITER_IDLE:
                writerIdleHandler(ctx);
                break;
            default:
                break;
        }
    }

    /**
     * 处理ping响应结果
     *
     * @param pong
     */
    protected void pongHandler(Message pong) {

    }

    /**
     * 客户端想服务端发送ping信息
     *
     * @param ctx
     */
    protected void sendPingToServer(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new Message(MessageType.PING));
    }

    /**
     * 服务端发送响应心跳数据
     *
     * @param ctx
     */
    private void sendPongToClient(ChannelHandlerContext ctx) {
        ctx.channel().writeAndFlush(new Message(MessageType.PONG));
    }


    public abstract void messageHandler(ChannelHandlerContext ctx, Message message);


    protected void allIdleHandler(ChannelHandlerContext ctx) {
    }

    protected void writerIdleHandler(ChannelHandlerContext ctx) {
    }

    protected void readerIdleHandler(ChannelHandlerContext ctx) {
    }

}
