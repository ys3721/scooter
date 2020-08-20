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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 这个地方没有想好，boss event loop 用来select事件然后  worker线程用来读取？
 * 明天在看这个吧
 *
 * @author: Yao Shuai
 * @date: 2020/8/19 17:10
 */
public class NIOWithSelectorAndGroup {

    public void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress(9100));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        ByteBuffer dest = ByteBuffer.allocate(1024);

        Executor boss = Executors.newFixedThreadPool(2);
        Executor worker = Executors.newFixedThreadPool(3);

        while (true) {
            selector.select();
            for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext(); ) {
                SelectionKey selectionKey = it.next();
                if(selectionKey.isAcceptable()) {
                    ServerSocketChannel acptChannel = (ServerSocketChannel) selectionKey.channel();
                    final SocketChannel clientChannel = acptChannel.accept();
                    if (clientChannel != null) {
                        boss.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clientChannel.configureBlocking(false);
                                    clientChannel.register(selector, SelectionKey.OP_READ);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

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
