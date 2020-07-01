package com.iceicelee.scooter.tools.natapp.client.consult;

import com.iceicelee.scooter.tools.logger.Loggers;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 用于和服务端协商的服务
 *
 * @author: Yao Shuai
 * @date: 2020/6/3 18:21
 */
public class ClientConsultService {

    private final String remoteServerIp;

    private final int remoteServerPort;

    public ClientConsultService(String remoteServerIp, int remoteServerPort) {
        this.remoteServerPort = remoteServerPort;
        this.remoteServerIp = remoteServerIp;
    }

    public void start() {
        Loggers.REVERSE_CLIENT.info("Begin connect the consult "+ this.getRemoteServerIp()
                +" port " + this.getRemoteServerPort());
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler())
                    .handler(new ClientConsultHandlerInitializer());
            ChannelFuture connectFuture = b.connect(this.getRemoteServerIp(), this.getRemoteServerPort());
        } catch (Exception e) {
            Loggers.REVERSE_CLIENT.error("链接失败, 请检查远端服务是不是已经启动？！", e);
        } finally {
        }
    }

    public String getRemoteServerIp() {
        return remoteServerIp;
    }

    public int getRemoteServerPort() {
        return remoteServerPort;
    }
}
