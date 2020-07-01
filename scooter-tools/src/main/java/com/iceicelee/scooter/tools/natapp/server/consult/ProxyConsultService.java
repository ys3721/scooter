package com.iceicelee.scooter.tools.natapp.server.consult;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.server.consult.handler.ConsultServerHandlerInitialization;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
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
        Loggers.REVERSE_LOGGER.info("Tunnel server 开始监听 " + this.listenPort + " 用于内部协商一些东西。");
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler())
                    .childHandler(new ConsultServerHandlerInitialization(this));
            ChannelFuture bindFuture = b.bind(this.getListenPort());
            bindFuture.addListener(future -> {
                if (future.isSuccess()) { {
                    Loggers.REVERSE_LOGGER.info("Tunnel server 监听 " + this.listenPort + " 用于内部协商一些东西成功！");
                }} else {
                    System.exit(-1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getListenPort() {
        return listenPort;
    }

    /**
     * 我在passive port等他来链接我
     *
     * @param passivePort
     * @param listenPort
     */
    public void comeConnectMeAt(int passivePort, int listenPort) {
/*        byte[] scHandshakeMsg = SCHandshakeMsg.newBuilder().setHandshakeResult(passivePort+","+listenPort).build().toByteArray();
        ByteBuf byteBuf = this.getConsultChannel().alloc().buffer(scHandshakeMsg.length);
        byteBuf.writeBytes(scHandshakeMsg);
        this.getConsultChannel().writeAndFlush(byteBuf);*/
    }

    public Channel getConsultChannel() {
        return consultChannel;
    }

    public void setConsultChannel(Channel consultChannel) {
        this.consultChannel = consultChannel;
    }

}