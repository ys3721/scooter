package com.iceicelee.scooter.tools.natapp.server.consult;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.message.HandshakeConsultMsg.SCHandshakeMsg;
import com.iceicelee.scooter.tools.natapp.server.consult.handler.ProxyConsultServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 17:40
 */
public class ProxyConsultService {

    private int listenPort;

    private Channel consultChannel;

    public ProxyConsultService() {
        this.listenPort = 5238;
    }

    public void start() {
        Thread t = new Thread(() -> {
            Loggers.REVERSE_LOGGER.info("Begin listen the consult port " + ProxyConsultService.this.getListenPort());
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler())
                        .childHandler(new ProxyConsultServerHandler(this))
                        .bind(ProxyConsultService.this.getListenPort()).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            }
            Loggers.REVERSE_LOGGER.info("ProxyConsultServer is shut down....");
        });
        t.start();

    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    /**
     * 我在passive port等他来链接我
     *
     * @param passivePort
     * @param listenPort
     */
    public void comeConnectMeAt(int passivePort, int listenPort) {
        byte[] scHandshakeMsg = SCHandshakeMsg.newBuilder().setHandshakeResult(passivePort+","+listenPort).build().toByteArray();
        ByteBuf byteBuf = this.getConsultChannel().alloc().buffer(scHandshakeMsg.length);
        byteBuf.writeBytes(scHandshakeMsg);
        this.getConsultChannel().writeAndFlush(byteBuf);
    }

    public Channel getConsultChannel() {
        return consultChannel;
    }

    public void setConsultChannel(Channel consultChannel) {
        this.consultChannel = consultChannel;
    }

}