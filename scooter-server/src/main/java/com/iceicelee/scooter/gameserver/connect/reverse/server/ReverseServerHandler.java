package com.iceicelee.scooter.gameserver.connect.reverse.server;

import com.iceicelee.scooter.gameserver.connect.reverse.message.HandshakeConsultMsg.CSHandshakeMsg;
import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:47
 */
public class ReverseServerHandler extends ChannelInboundHandlerAdapter {

    private int port;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //这个地方如果消息长度不够的话是有问题的
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        CSHandshakeMsg csHandshakeMsg = CSHandshakeMsg.parseFrom(bytes);
        int oldPort = port;
        this.port = csHandshakeMsg.getPort();
        Loggers.REVERSE_LOGGER.info(oldPort + " port is " + port);
        //建立一个port的监听
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            try {
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler())
                        .childHandler(new ProxyInitializer())
                        .bind(port).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
