package com.iceicelee.scooter.tools.natapp.client.consult;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.iceicelee.scooter.tools.natapp.client.config.ProtoMessageRecogenazer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import static io.netty.buffer.Unpooled.wrappedBuffer;

/**
 * @author: Yao Shuai
 * @date: 2020/6/30 20:30
 */
public class ProtobufWithMsgIdEncoder extends MessageToMessageEncoder<MessageLite> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out)
            throws Exception {
        MessageLite messageLite = ((MessageLite) msg);
        int messageNum = ProtoMessageRecogenazer.getMessageNum(msg);
        ByteBuf idBuf = Unpooled.buffer().writeInt(messageNum);
        out.add(idBuf);
        out.add(wrappedBuffer(messageLite.toByteArray()));
    }
}