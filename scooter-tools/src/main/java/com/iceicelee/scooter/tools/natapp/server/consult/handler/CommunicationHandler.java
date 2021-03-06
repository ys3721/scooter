package com.iceicelee.scooter.tools.natapp.server.consult.handler;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.server.external.ExternalServerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/4 11:44
 */
public class CommunicationHandler extends ChannelInboundHandlerAdapter {

    private final Channel externalChannel;

    private ChannelHandlerContext extralContext;

    public CommunicationHandler(Channel channel, ChannelHandlerContext ctx) {
        this.externalChannel = channel;
        this.extralContext = ctx;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Loggers.REVERSE_LOGGER.info("Client Back connection success!!");
        ExternalServerHandler exHandler = (ExternalServerHandler) extralContext.handler();
        exHandler.setInnerChannel(ctx.channel());
        extralContext.channel().read();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.REVERSE_LOGGER.info("Receive Client back to App MSg" + msg);
        externalChannel.writeAndFlush(msg);
        extralContext.channel().read();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

}
