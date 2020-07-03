package com.iceicelee.scooter.tools.natapp.server.consult.handler;

import com.iceicelee.scooter.tools.natapp.client.consult.ProtobufWithMsgIdDecoder;
import com.iceicelee.scooter.tools.natapp.client.consult.ProtobufWithMsgIdEncoder;
import com.iceicelee.scooter.tools.natapp.server.consult.ProxyConsultService;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author: Yao Shuai
 * @date: 2020/6/28 12:09
 */
public class ConsultServerHandlerInitialization extends ChannelInitializer<SocketChannel> {

    private ProxyConsultService consultService;

    public ConsultServerHandlerInitialization(ProxyConsultService service) {
        this.consultService = service;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufWithMsgIdDecoder());

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufWithMsgIdEncoder());

        p.addLast(new ProxyConsultServerHandler(consultService));
    }
}
