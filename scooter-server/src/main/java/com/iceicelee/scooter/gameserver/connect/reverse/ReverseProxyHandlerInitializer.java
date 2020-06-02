package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.connect.agent.HexDumpProxyFrontendHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:41
 */
public class ReverseProxyHandlerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new ReverseServerHandler());
    }
}
