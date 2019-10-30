package org.cooze.stepcounter.core.protocol.encrypt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.cooze.stepcounter.core.protocol.Message;
import org.cooze.stepcounter.core.utils.GzipUtil;
import org.cooze.stepcounter.core.utils.JsonUtil;

import java.util.List;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-12
 **/
public class MessageDecode extends ByteToMessageDecoder {

    private static int HEAD_LENGTH = 4;

    private static int ZERO_LENGTH = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        //这个HEAD_LENGTH是我们用于表示头长度的字节数。
        //由于上面我们传的是一个int类型的值，所以这里HEAD_LENGTH的值为4.
        if (byteBuf.readableBytes() < HEAD_LENGTH) {
            return;
        }
        //我们标记一下当前的readIndex的位置
        byteBuf.markReaderIndex();

        // 读取传送过来的消息的长度。ByteBuf 的readInt()方法会让他的readIndex增加4
        int dataLength = byteBuf.readInt();
        // 我们读到的消息体长度为0，这是不应该出现的情况，这里出现这情况，关闭连接。
        if (dataLength < ZERO_LENGTH) {
            ctx.close();
        }
        //读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (byteBuf.readableBytes() < dataLength) {
            byteBuf.resetReaderIndex();
            return;
        }
        //这时候，我们读到的长度，满足我们的要求了，把传送过来的数据，取出来吧~
        byte[] body = new byte[dataLength];
        byteBuf.readBytes(body);
        out.add(JsonUtil.fromJson(new String(GzipUtil.unGzip(body), CharsetUtil.UTF_8), Message.class));
    }
}
