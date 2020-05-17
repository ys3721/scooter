package com.iceicelee.scooter.gameserver.connect.agent;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/5/14 15:46
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel> {

    private final String remoteHost;

    private final int remotePort;

    public ProxyInitializer(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG), new ProxyFrontendHandler(remoteHost, remotePort));
    }
}
