package org.cooze.stepcounter.core.protocol.encrypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import org.cooze.stepcounter.core.protocol.Message;
import org.cooze.stepcounter.core.utils.GzipUtil;
import org.cooze.stepcounter.core.utils.JsonUtil;

/**
 * 编码器
 *
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public class MessageEncode extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf out) throws Exception {
        if (message == null) {
            return;
        }
        byte[] buff = JsonUtil.toJson(message).getBytes(CharsetUtil.UTF_8);
        byte[] zipBuff = GzipUtil.gzip(buff);
        out.writeInt(zipBuff.length);
        out.writeBytes(zipBuff);
    }
}
