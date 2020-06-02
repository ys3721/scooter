package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.connect.reverse.message.HandshakeConsultMsg.CSHandshakeMsg;
import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:47
 */
public class ReverseServerHandler extends ChannelInboundHandlerAdapter {

    private int port;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //这个地方如果消息长度不够的话是有问题的
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        CSHandshakeMsg csHandshakeMsg = CSHandshakeMsg.parseFrom(bytes);
        int oldPort = port;
        this.port = csHandshakeMsg.getPort();
        Loggers.REVERSE_LOGGER.info(oldPort + " port is " + port);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println(cause);
    }
}
