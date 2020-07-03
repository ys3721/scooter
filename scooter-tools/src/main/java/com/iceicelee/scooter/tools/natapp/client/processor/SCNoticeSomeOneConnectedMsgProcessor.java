package com.iceicelee.scooter.tools.natapp.client.processor;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto.SCNoticeSomeOneConnectedMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @author: Yao Shuai
 * @date: 2020/7/2 17:00
 */
public class SCNoticeSomeOneConnectedMsgProcessor {

    public void process(SCNoticeSomeOneConnectedMsg msg) {
        Loggers.CLIENT_CONSULT.debug(msg);
        int connectPort = msg.getIWantPort();
        int backConPort = msg.getConnectMePort();

        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

    }
}
