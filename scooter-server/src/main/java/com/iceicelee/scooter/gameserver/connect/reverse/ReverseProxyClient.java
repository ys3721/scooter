package com.iceicelee.scooter.gameserver.connect.reverse;

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
        options.addOption("l","localPort", true, "listen to the local port.");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.localPort = Integer.parseInt(cmd.getOptionValue("l"));
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("java -jar ReverseProxyClient.jar", header, options, footer, true);
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        ReverseProxyClient client = new ReverseProxyClient();
    }

}
