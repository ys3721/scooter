package com.iceicelee.scooter.gameserver.connect.reverse.server.consult.handler;

import com.iceicelee.scooter.gameserver.connect.reverse.message.HandshakeConsultMsg.CSHandshakeMsg;
import com.iceicelee.scooter.gameserver.connect.reverse.server.consult.ProxyConsultService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 18:07
 */
public class ProxyConsultServerHandler extends ChannelInboundHandlerAdapter {

    private final ProxyConsultService consultService;

    public ProxyConsultServerHandler(ProxyConsultService service) {
        this.consultService = service;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        this.consultService.setConsultChannel(channel);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //这个地方如果消息长度不够的话是有问题的
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        CSHandshakeMsg csHandshakeMsg = CSHandshakeMsg.parseFrom(bytes);
    }

}
