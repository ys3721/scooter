package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.connect.reverse.message.HandshakeConsultMsg.CSHandshakeMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/2 16:22
 */
public class ReverseClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
            for (int i = 0; i < 1000100; i++) {
                CSHandshakeMsg msg = CSHandshakeMsg.newBuilder().setPort(i).build();
                byte[] msgByte = msg.toByteArray();
                ByteBuf buf = ctx.alloc().buffer(msgByte.length).writeBytes(msgByte);
                ctx.channel().writeAndFlush(buf);
                Thread.sleep(100);
            }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println(cause);
    }
}
