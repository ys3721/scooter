package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * @author: Yao Shuai
 * @date: 2020/5/27 11:50
 */
public class ReverseProxyServer {

    private int listenPort;

    public static void main(String[] args) throws InterruptedException {
        ReverseProxyServer reversProxyServer = new ReverseProxyServer();
        reversProxyServer.readConfig(args);
        reversProxyServer.start();
    }

    private void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            try {
                b.group(bossGroup, workGroup)
                        .channel(NioServerSocketChannel.class)
                        .handler(new LoggingHandler())
                        .childHandler(new ReverseProxyHandlerInitializer())
                        .childOption(ChannelOption.AUTO_READ, true)
                        .bind(listenPort).sync().channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
        Loggers.REVERSE_LOGGER.info("Begin ！！！！！！！");
    }

    private void readConfig(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("l","localPort", true, "listen to the local port.");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.listenPort = Integer.parseInt(cmd.getOptionValue("l"));
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("java -jar ReversProxyServer.jar", header, options, footer, true);
            System.exit(-1);
        }
    }

}
