package com.iceicelee.scooter.tools.natapp.client.consult;

import com.iceicelee.scooter.tools.natapp.client.ClientContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * @author: Yao Shuai
 * @date: 2020/6/25 10:03
 */
public class ClientConsultHandlerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();

        p.addLast(new ProtobufVarint32FrameDecoder());
        p.addLast(new ProtobufWithMsgIdDecoder());

        p.addLast(new ProtobufVarint32LengthFieldPrepender());
        p.addLast(new ProtobufWithMsgIdEncoder());

        p.addLast(new ClientConsultHandler(ClientContext.getConsultService().getRemoteServerIp()));
    }
}
