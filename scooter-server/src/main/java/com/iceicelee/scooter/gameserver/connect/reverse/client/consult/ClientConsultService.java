package com.iceicelee.scooter.gameserver.connect.reverse.client.consult;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author: Yao Shuai
 * @date: 2020/6/3 18:21
 */
public class ClientConsultService {

    private String consultIp;

    private int consultPort;

    public ClientConsultService(String consultIp) {
        this.consultIp = consultIp;
        this.consultPort = 5238;
    }

    public void start() {
        Loggers.REVERSE_CLIENT.info("Begin connect the consult "+ this.getConsultIp() +" port " + this.getConsultPort());
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler())
                    .handler(new ClientConsultHandler(consultIp))
                    .connect(this.getConsultIp(), this.getConsultPort()).sync().channel().closeFuture().sync();
        } catch (Exception e) {
            Loggers.REVERSE_CLIENT.error("链接失败！", e);
        } finally {
            workGroup.shutdownGracefully();
        }
        Loggers.REVERSE_LOGGER.info("ClientConsultService is shut down....");
    }

    public String getConsultIp() {
        return consultIp;
    }

    public void setConsultIp(String consultIp) {
        this.consultIp = consultIp;
    }

    public int getConsultPort() {
        return consultPort;
    }

    public void setConsultPort(int consultPort) {
        this.consultPort = consultPort;
    }
}
