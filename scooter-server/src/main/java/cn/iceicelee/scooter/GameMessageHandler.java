package cn.iceicelee.scooter;

import cn.iceicelee.scooter.log.Loggers;
import cn.iceicelee.scooter.msg.GameMessage;
import cn.iceicelee.scooter.msg.GameMessage.CGUserEntry;
import io.netty.buffer.ByteBuf;
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

    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.GAME_LOG.debug("收到客户端都消息"  + msg.getClass().getName());

        if (msg instanceof GameMessage.CGUserEntry) {
            GameMessage.CGUserEntry cgEntryMsg = (CGUserEntry) msg;
        }
    }
}
