package com.iceicelee.scooter.gameserver.connect.reverse;

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

    public static void main(String[] args) {
        ReverseProxyServer reversProxyServer = new ReverseProxyServer();
        reversProxyServer.readConfig(args);
        reversProxyServer.start();
    }

    private void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler())
                .childHandler(new ReverseProxyHandlerInitializer())
                .childOption(ChannelOption.AUTO_READ, false)
                .bind(this.listenPort);
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
