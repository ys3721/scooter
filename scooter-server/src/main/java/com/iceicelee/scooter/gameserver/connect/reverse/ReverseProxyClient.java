package com.iceicelee.scooter.gameserver.connect.reverse;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * @author: Yao Shuai
 * @date: 2020/6/1 15:40
 */
public class ReverseProxyClient {

    private String serverIp;

    private int serverPort;

    private int localPort;

    public ReverseProxyClient(String[] args) {
        this.init(args);
    }

    private void init(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("l", "localPort", true, "listen to the local port.");
        options.addOption("p", "remotePort", true, "connect to the remote port.");
        options.addOption("h", "remoteAddress", true, "connect to the remote address.");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.localPort = Integer.parseInt(cmd.getOptionValue("l"));
            this.serverPort = Integer.parseInt(cmd.getOptionValue("p"));
            this.serverIp = cmd.getOptionValue('h');
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("java -jar ReverseProxyClient.jar", header, options, footer, true);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        ReverseProxyClient client = new ReverseProxyClient(args);
        client.connect();
    }

    private void connect() {
        try {
            EventLoopGroup work = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(work).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ReverseClientHandler());
                }
            });
            ChannelFuture f = bootstrap.connect(this.serverIp, this.serverPort).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Loggers.REVERSE_LOGGER.error("连接服务器报错！", e);
            System.exit(-1);
        } finally {
        }
    }

}
