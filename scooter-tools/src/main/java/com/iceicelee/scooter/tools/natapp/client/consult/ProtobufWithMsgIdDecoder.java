package com.iceicelee.scooter.tools.natapp.client.consult;

import com.google.protobuf.MessageLite;
import com.iceicelee.scooter.tools.natapp.client.config.ProtoMessageRecogenazer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.awt.dnd.DragGestureRecognizer;
import java.util.List;

/**
 * @author: Yao Shuai
 * @date: 2020/6/29 23:11
 */
public class ProtobufWithMsgIdDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        final byte[] array;
        final int offset;
        final int length = msg.readableBytes();
        final int msgNumber = msg.readInt();
        if (msg.hasArray()) {
            array = msg.array();
            offset = msg.arrayOffset() + msg.readerIndex();
        } else {
            array = ByteBufUtil.getBytes(msg, msg.readerIndex(), length, false);
            offset = 0;
        }
        out.add(ProtoMessageRecogenazer.getDefaultLiteById(msgNumber).getParserForType()
                .parseFrom(array, offset, length));
    }

}
