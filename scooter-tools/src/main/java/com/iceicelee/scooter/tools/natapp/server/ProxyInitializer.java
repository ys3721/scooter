package com.iceicelee.scooter.tools.natapp.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 17:16
 */
public class ProxyInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ProxyServerHandler());
    }
}
