package com.iceicelee.scooter.tools.natapp.client;

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

    public ReverseProxyClient(String[] args) {
        this.initConfig(args);
    }

    public static void main(String[] args) {
        ReverseProxyClient client = new ReverseProxyClient(args);
        client.init(args);
        client.start();
    }

    private void init(String[] args) {
        ReverseClientConfig clientConfig = this.initConfig(args);
        this.initContext(clientConfig);

    }

    private void initContext(ReverseClientConfig clientConfig) {
        ClientContext.initContext(clientConfig);
    }

    private void start() {
        ClientContext.getConsultService().start();
    }

    private ReverseClientConfig initConfig(String[] args) {
        String serverIp = null;
        String lanAppIp = null;
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("h", "remoteAddress", true,
                "connect to the remote proxy server address.");
        options.addOption("t", "target lan ip", true,
                "Lan ip where to proxy to.");
        try {
            CommandLine cmd = parser.parse(options, args);
            serverIp = cmd.getOptionValue('h');
            lanAppIp = cmd.getOptionValue('t');
            if (serverIp == null || lanAppIp == null) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("sh scooter-server ", header, options, footer, true);
            System.exit(-1);
        }
        return  new ReverseClientConfig(serverIp, lanAppIp);
    }
}
