package com.iceicelee.scooter.tools.natapp.server.external;

import com.iceicelee.scooter.tools.natapp.server.ReverseProxyServer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:41
 */
public class ExternalProxyHandlerInitializer extends ChannelInitializer<SocketChannel> {

    private ReverseProxyServer server;

    public ExternalProxyHandlerInitializer(ReverseProxyServer server) {
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ExternalServerHandler(server));
    }

}
