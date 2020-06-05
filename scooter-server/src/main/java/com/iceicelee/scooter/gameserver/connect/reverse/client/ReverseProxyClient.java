package com.iceicelee.scooter.gameserver.connect.reverse.client;

import com.iceicelee.scooter.gameserver.connect.reverse.client.consult.ClientConsultService;
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

    public ReverseProxyClient(String[] args) {
        this.initConfig(args);
    }

    public static void main(String[] args) {
        ReverseProxyClient client = new ReverseProxyClient(args);
        client.startConsultService();
    }

    private void startConsultService() {
        ClientConsultService clientConsultService = new ClientConsultService(this.serverIp);
        clientConsultService.start();
    }


    private void initConfig(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("h", "remoteAddress", true, "connect to the remote address.");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.serverIp = cmd.getOptionValue('h');
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("java -jar ReverseProxyClient.jar", header, options, footer, true);
            System.exit(-1);
        }
    }
}
