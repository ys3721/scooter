package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.connect.reverse.message.HandshakeConsultMsg.CSHandshakeMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:47
 */
public class ReverseServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 当链接到来的时候 开始读取
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.read();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
        CSHandshakeMsg.newBuilder().setPort(80);
    }

}
