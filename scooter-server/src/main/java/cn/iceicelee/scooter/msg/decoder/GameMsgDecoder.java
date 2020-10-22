package cn.iceicelee.scooter.msg.decoder;

import cn.iceicelee.scooter.log.Loggers;
import cn.iceicelee.scooter.msg.GameMessage;
import cn.iceicelee.scooter.msg.GameMessage.MsgCode;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import static cn.iceicelee.scooter.msg.GameMessage.MsgCode.CG_USER_ENTRY_VALUE;
import static cn.iceicelee.scooter.msg.GameMessage.MsgCode.CG_WHO_ELSE_IS_HERE_VALUE;


/**
 * @author: Yao Shuai
 * @date: 2020/10/14 18:32
 */
public class GameMsgDecoder extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        Loggers.GAME_LOG.debug("收到h5frame ->"  + msg);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        short msgLength = byteBuf.readShort();
        short msgId = byteBuf.readShort();

        byte[] byteArrays = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(byteArrays);

        GeneratedMessageV3 gameMsg = null;

        switch (msgId) {
            case CG_USER_ENTRY_VALUE:
                gameMsg = GameMessage.CGUserEntry.parseFrom(byteArrays);
                break;
            case CG_WHO_ELSE_IS_HERE_VALUE:
                break;
        }
        if (gameMsg != null) {
            channelHandlerContext.fireChannelRead(gameMsg);
            Loggers.GAME_LOG.debug(gameMsg);
        } else {
            Loggers.GAME_LOG.error("未知的接收消息 " + msgId);
        }
    }


}
