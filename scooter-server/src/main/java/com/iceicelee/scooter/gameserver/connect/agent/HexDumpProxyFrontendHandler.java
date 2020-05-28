package com.iceicelee.scooter.gameserver.connect.agent;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: Yao Shuai
 * @date: 2020/5/18 12:30
 */
public class HexDumpProxyFrontendHandler extends ChannelInboundHandlerAdapter {

    private String remoteAddress;

    private int remotePort;

    private Channel outboundChannel;

    public HexDumpProxyFrontendHandler(String remoteAddress, int remotePort) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    /**
     *当这里有一个新的连接到来的时候，就去远程建立一个链接，用于把当前channel的数据转发到远端
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws InterruptedException {
        final Channel inboundChannel = ctx.channel();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(inboundChannel.eventLoop());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new HexDumpProxyBackendHandler(inboundChannel));
        bootstrap.option(ChannelOption.AUTO_READ, false);
        ChannelFuture f = bootstrap.connect(remoteAddress, remotePort);
        this.outboundChannel = f.channel();
        f.addListener(future -> {
                if(future.isSuccess()) {
                    inboundChannel.read();
                } else {
                    inboundChannel.close();
                }
        });
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChannelFuture f = this.outboundChannel.writeAndFlush(msg);
        f.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                ctx.channel().read();
            } else {
                ctx.channel().close();
            }
        });
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        if (outboundChannel != null) {
            if(outboundChannel.isActive()) {
                outboundChannel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

}
