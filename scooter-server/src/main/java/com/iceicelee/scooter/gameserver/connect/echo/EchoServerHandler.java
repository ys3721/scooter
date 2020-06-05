package com.iceicelee.scooter.gameserver.connect.echo;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/5 12:09
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Loggers.ECHO.info("Echo server connect establisthed!!");
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.ECHO.info("Echo server receive msg :" + msg);
        ctx.writeAndFlush(msg);
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        Loggers.ECHO.info("Echo server error :" + cause.getMessage());
        ctx.close();
    }

}
