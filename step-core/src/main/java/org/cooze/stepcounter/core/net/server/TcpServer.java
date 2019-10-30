package org.cooze.stepcounter.core.net.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.cooze.stepcounter.core.net.channel.MessageChannelServer;
import org.cooze.stepcounter.core.protocol.encrypt.MessageDecode;
import org.cooze.stepcounter.core.protocol.encrypt.MessageEncode;
import org.cooze.stepcounter.core.utils.StringUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author cooze
 * @version 1.0.0 创建于 2019-08-13
 **/
public class TcpServer {

    private String host;
    private int port;
    private Integer workThreads;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private MessageHandler handler;

    public void setHandler(MessageHandler handler) {
        this.handler = handler;
    }

    public TcpServer(String host, int port, Integer workThreads) {
        this.port = port;
        this.host = host;
        this.workThreads = workThreads;
    }

    public TcpServer(String host, int port) {
        this(host, port, null);
    }

    public void start() {
        startUpServer();
    }


    public void close() {
        if (bossGroup != null &&
                !bossGroup.isShutdown()) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null &&
                !workerGroup.isShutdown()) {
            workerGroup.shutdownGracefully();
        }
    }

    private void startUpServer() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(this.workThreads != null ? this.workThreads : Runtime.getRuntime().availableProcessors() * 2);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        if (StringUtils.isEmpty(this.host)) {
            serverBootstrap.localAddress(new InetSocketAddress(port));
        } else {
            serverBootstrap.localAddress(new InetSocketAddress(host, port));
        }
        serverBootstrap.group(bossGroup, workerGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        //保持连接
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) throws Exception {
                channel.pipeline()
                        .addLast("ping", new IdleStateHandler(1, 0, 5, TimeUnit.SECONDS))
                        .addLast("decoder", new MessageDecode())
                        .addLast("encoder", new MessageEncode())
                        .addLast("handler", new MessageChannelServer(handler == null ? new DefaultMessageHandler() : handler));
            }
        });
        try {
            ChannelFuture sync = serverBootstrap.bind().sync();
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public static void main(String[] args) {
        new TcpServer("127.0.0.1", 9090).start();
    }
}
