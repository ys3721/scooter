package cn.iceicelee.scooter;

import cn.iceicelee.scooter.cn.iceicelee.scooter.log.Loggers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author yaoshuai
 */
public class GameMessageHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.GAME_LOG.debug("收到客户端都消息"  + msg);

        BinaryWebSocketFrame frame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = frame.content();

        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        for (byte b : bytes) {
            System.out.print(b);
            System.out.print(",");
        }
        System.out.println();
    }
}
