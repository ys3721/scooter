package com.iceicelee.scooter.tools.natapp.client;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.iceicelee.scooter.tools.natapp.client.config.CommandLineArgsHelper;
import com.iceicelee.scooter.tools.natapp.client.config.ReverseClientConfig;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto.CSHandshakeMsg;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

/**
 * 客户端
 *
 * @author: Yao Shuai
 * @date: 2020/6/1 15:40
 */
public class TunnelsProxyClient {

    public TunnelsProxyClient(String[] args) {
        this.initConfig(args);
    }

    public static void main(String[] args) {
        FileDescriptor fileDescriptor = ConsultMessageProto.getDescriptor();
        System.out.println(fileDescriptor);
        TunnelsProxyClient client = new TunnelsProxyClient(args);
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
        return CommandLineArgsHelper.initConfig(args);
    }
}
