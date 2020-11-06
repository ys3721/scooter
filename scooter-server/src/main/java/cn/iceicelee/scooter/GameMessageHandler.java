package cn.iceicelee.scooter;

import cn.iceicelee.scooter.log.Loggers;
import cn.iceicelee.scooter.msg.GameMessage;
import cn.iceicelee.scooter.msg.GameMessage.CGUserEntry;
import cn.iceicelee.scooter.msg.GameMessage.CGUserMoveTo;
import cn.iceicelee.scooter.msg.GameMessage.GCUserEntry;
import cn.iceicelee.scooter.msg.GameMessage.GCUserMoveToResult;
import cn.iceicelee.scooter.msg.GameMessage.GCUserQuitResult;
import cn.iceicelee.scooter.msg.GameMessage.GCWhoElseIsHere;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yaoshuai
 */
public class GameMessageHandler extends SimpleChannelInboundHandler<Object> {

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final Map<Integer, User> userMap = new HashMap<Integer, User>();

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

            userMap.put(userId, new User(userId, avatarStr));
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            GCUserEntry.Builder entryMsgBuilder = GCUserEntry.newBuilder();
            entryMsgBuilder.setUserId(userId);
            entryMsgBuilder.setHeroAvatar(avatarStr);
            GCUserEntry userEntryMsg = entryMsgBuilder.build();

            CHANNEL_GROUP.writeAndFlush(userEntryMsg);
        } else if (msg instanceof  GameMessage.CGWhoElseIsHere) {
            GCWhoElseIsHere.Builder whoElseIsHereMsg = GCWhoElseIsHere.newBuilder();
            for (User user : userMap.values()) {
                GCWhoElseIsHere.UserInfo.Builder userInfoBuilder = GCWhoElseIsHere.UserInfo.newBuilder();
                userInfoBuilder.setUserId(user.getUserId());
                userInfoBuilder.setHeroAvatar(user.getHeroAvatar());
                whoElseIsHereMsg.addUserInfo(userInfoBuilder.build());
            }
            ctx.channel().writeAndFlush(whoElseIsHereMsg.build());
        } else if (msg instanceof GameMessage.CGUserMoveTo) {
            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (userId == null) {
                return;
            }
            GCUserMoveToResult.Builder builder = GameMessage.GCUserMoveToResult.newBuilder();
            builder.setMoveUserId(userId);
            builder.setMoveToPosX(((CGUserMoveTo) msg).getMoveToPosX());
            builder.setMoveToPosY(((CGUserMoveTo) msg).getMoveToPosY());
            CHANNEL_GROUP.writeAndFlush(builder.build());
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (userId == null) {
            return ;
        }
        CHANNEL_GROUP.remove(ctx.channel());
        userMap.remove(userId);
        Loggers.GAME_LOG.debug("从地图中移除了玩家id：" + userId);
        GCUserQuitResult.Builder quitResultBuilder = GCUserQuitResult.newBuilder();
        quitResultBuilder.setQuitUserId(userId);
        CHANNEL_GROUP.writeAndFlush(quitResultBuilder);
    }

}
