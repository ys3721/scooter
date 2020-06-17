package com.iceicelee.scooter.gameserver.connect.reverse.client.consult;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author: Yao Shuai
 * @date: 2020/6/4 17:13
 */
public class ClientConmmunicationHandler extends ChannelInboundHandlerAdapter {

    private Channel appChannel;

    private ClientAppLocalHandler appLocalHandler;

    public ClientConmmunicationHandler(Channel appChannel, ClientAppLocalHandler appLocalHandler) {
        this.appChannel = appChannel;
        this.appLocalHandler = appLocalHandler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Loggers.REVERSE_CLIENT.info("Back connection channel success!");
        appLocalHandler.setCommunacationChannel(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.REVERSE_CLIENT.info("Back trace channel msg comming!");
        appChannel.writeAndFlush(msg);
        appChannel.read();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

}
