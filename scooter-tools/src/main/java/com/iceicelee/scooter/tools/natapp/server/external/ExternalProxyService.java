package com.iceicelee.scooter.tools.natapp.server.external;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.server.ReverseProxyServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 17:52
 */
public class ExternalProxyService {

    private final ReverseProxyServer server;

    private int listenPort;

    public ExternalProxyService(int listenPort, ReverseProxyServer server) {
        this.listenPort = listenPort;
        this.server = server;
    }

    public void start() {
        Loggers.REVERSE_LOGGER.info("Tunnel server 外部开始监听 " + this.listenPort
                + " 用于把外部的链接转发到内网。");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ExternalProxyHandlerInitializer(server))
                    .childOption(ChannelOption.AUTO_READ, false)
                    .bind(listenPort).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
