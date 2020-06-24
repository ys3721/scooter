package com.iceicelee.scooter.tools.natapp.server;

import com.iceicelee.scooter.tools.natapp.message.HandshakeConsultMsg.CSHandshakeMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/2 16:22
 */
public class ReverseClientHandler extends ChannelInboundHandlerAdapter {

    private int clientPort = 3306;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CSHandshakeMsg msg = CSHandshakeMsg.newBuilder().setPort(clientPort).build();
        byte[] msgByte = msg.toByteArray();
        ByteBuf buf = ctx.alloc().buffer(msgByte.length).writeBytes(msgByte);
        ctx.channel().writeAndFlush(buf);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println(cause);
    }
}
