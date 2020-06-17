package com.iceicelee.scooter.gameserver.connect.reverse.server.external;

import com.iceicelee.scooter.gameserver.connect.reverse.server.ReverseProxyServer;
import com.iceicelee.scooter.gameserver.connect.reverse.server.consult.handler.CommunicationHandler;
import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 11:47
 */
public class ExternalServerHandler extends ChannelInboundHandlerAdapter {

    private final ReverseProxyServer server;

    private Channel innerChannel;

    public ExternalServerHandler(ReverseProxyServer server) {
        this.server = server;
    }

    /**
     * 当游戏服务器来了的时候 我要开一个端口然后通知client连这个端口
     * 连上来之后把这个hander的数据全都转发的client的链接上
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        int port = (int) (Math.random()* 10000);
        Loggers.REVERSE_LOGGER.info("Someone coming...");
        EventLoopGroup workGroup = new NioEventLoopGroup();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        try {
            Loggers.REVERSE_LOGGER.info("Someone coming...listen on port " + port);
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CommunicationHandler(ctx.channel(), ctx));
                        }
                    });
            ChannelFuture cf = b.bind(port);
            cf.sync();
            cf.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //然后通知客户端来连这个端口
        server.sendSomeComming(port);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Loggers.REVERSE_LOGGER.info("Receive App msg " + msg);
        innerChannel.writeAndFlush(msg);
        ctx.channel().read();
    }

    public Channel getInnerChannel() {
        return innerChannel;
    }

    public void setInnerChannel(Channel innerChannel) {
        this.innerChannel = innerChannel;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
}
