package com.iceicelee.scooter.gameserver.connect.agent;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;

/**
 * @author: Yao Shuai
 * @date: 2020/5/17 23:52
 */
public class HexDumpProxyServer {

    private int localPort;

    private String remoteHost;

    private int remotePort;

    private final Logger loggers = LogManager.getLogger(HexDumpProxyServer.class);

    public static void main(String[] args) {
        HexDumpProxyServer proxyServer = new HexDumpProxyServer();
        proxyServer.initAddress(args);
        proxyServer.beginAddListen();
    }

    private void beginAddListen() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            try {
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler())
                        .childHandler(new HexDumpProxyInitializer(remoteHost, remotePort))
                        .childOption(ChannelOption.AUTO_READ, false)
                        .bind(localPort).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        loggers.debug("Begin ！！！！！！！");
    }

    private void initAddress(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("l","localPort", true, "listen to the local port");
        options.addOption("h","remoteHost", true, "send to the local port");
        options.addOption("p","remotePort", true, "send to the local port");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.localPort = Integer.parseInt(cmd.getOptionValue("l"));
            this.remoteHost = cmd.getOptionValue("h");
            this.remotePort = Integer.parseInt(cmd.getOptionValue('p'));
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar HexDumpProxyServer.jar", options);
        }
    }
}
