package com.iceicelee.scooter.tools.natapp.client.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * @author: Yao Shuai
 * @date: 2020/6/24 12:07
 */
public class CommandLineArgsHelper {

    public static ReverseClientConfig initConfig(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("r", "remote Server Ip", true,
                "connect to the remote proxy server address. 远程服务器端的ip。");
        options.addOption("p", "remote Server Port", true,
                "connect to the remote proxy server port.远程服务端的端口地址。");
        options.addOption("l", "local lan target Ip", true,
                "The lan ip of which you wan tunnel to. 本地内网的ip地址，" +
                        "远端传来的数据会转发到这个地址，一般127.0.0.1转发到本地。");
        try {
            CommandLine cmd = parser.parse(options, args);
            String serverIp = cmd.getOptionValue('r');
            String serverPort = cmd.getOptionValue('p');
            String lanAppIp = cmd.getOptionValue('l');
            if (serverIp == null || lanAppIp == null) {
                throw new IllegalArgumentException();
            }
            return  new ReverseClientConfig(serverIp, Integer.parseInt(serverPort), lanAppIp);
        } catch (Exception e) {
            HelpFormatter formatter = new HelpFormatter();
            String header = "This app will listen the port you give:\n\n";
            String footer = "\nPlease report issues at http://www.iceicelee.com/issues";
            formatter.printHelp("sh scooter-server ", header, options, footer, true);
            System.exit(-1);
        }
        return null;
    }

}
