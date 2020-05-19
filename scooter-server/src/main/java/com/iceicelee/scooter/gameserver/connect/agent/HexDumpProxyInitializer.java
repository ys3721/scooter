package com.iceicelee.scooter.gameserver.connect.agent;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @author: Yao Shuai
 * @date: 2020/5/18 12:26
 */
public class HexDumpProxyInitializer extends ChannelInitializer<SocketChannel> {

    private final String remoteAddress;

    private final int remotePort;

    public HexDumpProxyInitializer(String remoteAddress, int remotePort) {
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new HexDumpProxyFrontendHandler(remoteAddress, remotePort));
    }
}
