package com.iceicelee.scooter.gameserver.connect.reverse.client.consult;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/4 17:20
 */
public class ClientAppLocalHandler extends ChannelInboundHandlerAdapter {

    private Channel communacationChannel;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        communacationChannel.writeAndFlush(msg);
    }


    public Channel getCommunacationChannel() {
        return communacationChannel;
    }

    public void setCommunacationChannel(Channel communacationChannel) {
        this.communacationChannel = communacationChannel;
    }
}
