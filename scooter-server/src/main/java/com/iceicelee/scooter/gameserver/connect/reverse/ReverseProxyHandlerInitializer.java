package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.connect.agent.HexDumpProxyFrontendHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:41
 */
public class ReverseProxyHandlerInitializer extends ChannelInitializer {

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new ReverseServerHandler());
    }
}
