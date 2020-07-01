package com.iceicelee.scooter.tools.natapp.server.consult.handler;

import com.google.protobuf.MessageLite;
import com.iceicelee.scooter.tools.natapp.client.config.ProtoMessageRecogenazer;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;
import com.iceicelee.scooter.tools.natapp.server.consult.ProxyConsultService;
import com.iceicelee.scooter.tools.natapp.server.processor.CSHandshakeMsgProcessorFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 18:07
 */
public class ProxyConsultServerHandler extends ChannelInboundHandlerAdapter {

    public ProxyConsultServerHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageLite messageLite = (MessageLite) msg;
        //先这么写八
        switch (ProtoMessageRecogenazer.getMessageNum(messageLite)){
            case 1001:
                CSHandshakeMsgProcessorFactory.getProcessor().process((ConsultMessageProto.CSHandshakeMsg) msg);
                break;
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
