package com.iceicelee.scooter.gameserver.connect.reverse.server;

import com.iceicelee.scooter.gameserver.connect.reverse.server.consult.ProxyConsultService;
import com.iceicelee.scooter.gameserver.connect.reverse.server.external.ExternalProxyService;
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

    private ProxyConsultService consultService;

    private ExternalProxyService externalProxyServer;

    private int listenPort;

    public static void main(String[] args) {
        ReverseProxyServer reversProxyServer = new ReverseProxyServer();
        reversProxyServer.readConfig(args);
        reversProxyServer.startConsultChannel();
        reversProxyServer.startExternalChannel();
    }

    private void startExternalChannel() {
        externalProxyServer = new ExternalProxyService(this.getListenPort(), this);
        externalProxyServer.start();
    }

    private void startConsultChannel() {
        consultService = new ProxyConsultService();
        consultService.start();
    }

    private void readConfig(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("l", "localPort", true,
                "proxy the port to the local port.");
        try {
            CommandLine cmd = parser.parse(options, args);
            this.listenPort = Integer.parseInt(cmd.getOptionValue("l"));
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("java -jar ReversProxyServer.jar",
                    header, options, footer, true);
            System.exit(-1);
        }
    }

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public void sendSomeComming(int port) {
        this.consultService.comeConnectMeAt(port, listenPort);
    }
}
