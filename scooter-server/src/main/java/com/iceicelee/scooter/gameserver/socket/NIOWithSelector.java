package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: Yao Shuai
 * @date: 2020/8/17 21:09
 */
public class NIOWithSelector {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(9100));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer dest = ByteBuffer.allocate(1024);

        while (true) {
            selector.select();
            for (Iterator<SelectionKey> it =  selector.selectedKeys().iterator(); it.hasNext(); ) {
                SelectionKey selectionKey = it.next();
                if(selectionKey.isAcceptable()) {
                    ServerSocketChannel acptChannel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel clientChannel = acptChannel.accept();
                    if (clientChannel != null) {
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    }
                } else if (selectionKey.isReadable()) {
                    SocketChannel readChannel = (SocketChannel) selectionKey.channel();
                    readChannel.read(dest);
                    dest.flip();
                    byte[] dst = new byte[dest.limit()];
                    dest.get(dst);
                    Loggers.SERVER_LOGGER.info("RECEIVE -> " + new String(dst));
                    dest.clear();
                    dest.put(dst).flip();
                    readChannel.write(dest);
                    readChannel.write(ByteBuffer.wrap(new String("\n").getBytes()));
                    dest.clear();
                }
                it.remove();
            }
        }
    }

}
