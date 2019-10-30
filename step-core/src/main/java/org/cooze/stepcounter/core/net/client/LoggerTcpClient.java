package org.cooze.stepcounter.core.net.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.cooze.stepcounter.core.net.channel.MessageChannelClient;
import org.cooze.stepcounter.core.protocol.Message;
import org.cooze.stepcounter.core.protocol.encrypt.MessageDecode;
import org.cooze.stepcounter.core.protocol.encrypt.MessageEncode;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import static org.cooze.stepcounter.core.protocol.MessageType.PING;
import static org.cooze.stepcounter.core.protocol.MessageType.PONG;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public class LoggerTcpClient {
    private NioEventLoopGroup worker;
    private Channel channel;
    private Bootstrap bootstrap;
    private String host;
    private int port;
    private Long timeout;
    private final long DEFAULT_TIMEOUT = 5000;

    private ClientResult clientResult = new ClientResult();


    private void lock(long timeout) throws InterruptedException {
        wait(timeout);
    }

    private void unlock() {
        notifyAll();
    }

    public LoggerTcpClient(String host, int port) {
        this(host, port, null);
    }

    public LoggerTcpClient(String host, int port, Long timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout == null ? DEFAULT_TIMEOUT : timeout;
        worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast("ping", new IdleStateHandler(10, 2, 2, TimeUnit.SECONDS))
                                .addLast("encoder", new MessageEncode())
                                .addLast("decoder", new MessageDecode())
                                .addLast("handler", new MessageChannelClient(clientResult));
                    }
                });
    }

    public boolean connect() throws Exception {
        final boolean[] isConn = {Boolean.FALSE};
        if (channel != null && channel.isActive()) {
            isConn[0] = Boolean.TRUE;
            return isConn[0];
        }
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(host, port));
        //实现监听通道连接的方法
        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                synchronized (LoggerTcpClient.this) {
                    if (channelFuture.isSuccess()) {
                        channel = channelFuture.channel();
                        isConn[0] = Boolean.TRUE;
                        unlock();
                    } else {
                        //重连操作
                        channelFuture.channel().eventLoop().schedule(() -> bootstrap.connect(host, port), 1L, TimeUnit.SECONDS);
                    }
                }
            }
        });
        synchronized (this) {
            long start = System.currentTimeMillis();
            lock(this.timeout);
            long end = System.currentTimeMillis();
            if ((end - start) >= this.timeout) {
                worker.shutdownGracefully(1, 1, TimeUnit.NANOSECONDS);
                throw new Exception("链接超时！");
            }
        }
        return isConn[0];
    }

    public void send(Message message) {
        if (channel == null || !channel.isActive()) {
            return;
        }
        channel.writeAndFlush(message);
    }

    public boolean ping(long timeout) throws Exception {
        if (channel == null || !channel.isActive()) {
            return false;
        }
        channel.writeAndFlush(new Message(PING));
        Message message = clientResult.get(timeout);
        if (message != null && message.getType().compareTo(PONG) == 0) {
            return true;
        }
        return false;

    }

    public void close() {
        if (worker == null || worker.isShutdown()) {
            return;
        }
        worker.shutdownGracefully(1, 1, TimeUnit.NANOSECONDS);
    }

    public static void main(String[] args) throws Exception {
        LoggerTcpClient tcpClient = new LoggerTcpClient("127.0.0.1", 9090);
        boolean isConn = tcpClient.connect();
        if (isConn) {
            System.out.println(isConn);
        }
        System.out.println(tcpClient.ping(1000));
        tcpClient.ping(1000);
    }
}
