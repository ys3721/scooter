package cn.iceicelee.scooter.msg.encoder;

import cn.iceicelee.scooter.log.Loggers;
import cn.iceicelee.scooter.msg.GameMessage;
import cn.iceicelee.scooter.msg.GameMessage.GCUserEntry;
import cn.iceicelee.scooter.msg.GameMessage.MsgCode;
import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author: Yao Shuai
 * @date: 2020/10/22 16:08
 */
public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof GeneratedMessageV3)) {
            ctx.writeAndFlush(msg);
            return;
        }
        int msgCode = -1;
        if (msg instanceof GameMessage.GCUserEntry) {
            msgCode = GameMessage.MsgCode.GC_USER_ENTRY_VALUE;
        } else if (msg instanceof  GameMessage.GCWhoElseIsHere) {
            msgCode = MsgCode.GC_WHO_ELSE_IS_HERE_VALUE;
        } else if (msg instanceof  GameMessage.GCUserMoveToResult) {
            msgCode = MsgCode.GC_USER_MOVE_TO_RESULT_VALUE;
        } else if (msg instanceof  GameMessage.GCUserQuitResult) {
            msgCode = MsgCode.GC_USER_QUIT_RESULT_VALUE;
        } else {
            Loggers.GAME_LOG.error("未知的发送消息 " + msgCode);
            return;
        }
        GeneratedMessageV3 v3Msg = (GeneratedMessageV3) msg;
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeShort(0);
        byteBuf.writeShort(msgCode);
        byteBuf.writeBytes(v3Msg.toByteArray());

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        Loggers.GAME_LOG.debug("发送消息 " + msgCode);
        super.write(ctx, frame, promise);
    }
}
