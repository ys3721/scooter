package com.iceicelee.scooter.tools.natapp.server.consult.handler;

import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;
import com.iceicelee.scooter.tools.natapp.server.consult.ProxyConsultService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author: Yao Shuai
 * @date: 2020/6/28 12:09
 */
public class ConsultServerHandlerInitialization extends ChannelInitializer<SocketChannel> {

    private final ProxyConsultService consultService;

    public ConsultServerHandlerInitialization(ProxyConsultService service) {
        consultService = service;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufDecoder(ConsultMessageProto.CSHandshakeMsg.getDefaultInstance()));

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufEncoder());

        p.addLast(new ProxyConsultServerHandler(null));
    }
}
