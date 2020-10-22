package cn.iceicelee.scooter;

import cn.iceicelee.scooter.log.Loggers;
import cn.iceicelee.scooter.msg.GameMessage;
import cn.iceicelee.scooter.msg.GameMessage.CGUserEntry;
import cn.iceicelee.scooter.msg.GameMessage.GCUserEntry;
import cn.iceicelee.scooter.msg.GameMessage.GCUserEntry.Builder;
import cn.iceicelee.scooter.msg.decoder.GameMsgDecoder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author yaoshuai
 */
public class GameMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        CHANNEL_GROUP.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.GAME_LOG.debug("收到客户端消息"  + msg.getClass().getName());

        if (msg instanceof GameMessage.CGUserEntry) {
            GameMessage.CGUserEntry cgEntryMsg = (CGUserEntry) msg;
            int userId = cgEntryMsg.getUserId();
            String avatarStr = cgEntryMsg.getHeroAvatar();

            Builder entryMsgBuilder = GCUserEntry.newBuilder();
            entryMsgBuilder.setUserId(userId);
            entryMsgBuilder.setHeroAvatar(avatarStr);

            GCUserEntry userEntryMsg = entryMsgBuilder.build();

            CHANNEL_GROUP.writeAndFlush(userEntryMsg);
        }
    }
}
