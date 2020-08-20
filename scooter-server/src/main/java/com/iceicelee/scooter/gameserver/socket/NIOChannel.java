package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

/**
 * @author: Yao Shuai
 * @date: 2020/8/17 18:21
 */
public class NIOChannel {


    /**
     * 这个还是用一个但线程，只有一个主线程，但是把阻塞去掉了。
     * 所以不是，这里并没有用selector模型。
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Queue<SocketChannel> socketChannels = new ArrayDeque();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9100));
        serverSocketChannel.configureBlocking(false);
        while (true) {
            Thread.sleep(1000);
            SocketChannel channel = serverSocketChannel.accept();
            Loggers.SERVER_LOGGER.info("This place not block by accept!");
            if (channel != null) {
                socketChannels.add(channel);
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            for (Iterator<SocketChannel> channelIterator = socketChannels.iterator(); channelIterator.hasNext(); ) {
                SocketChannel socketChannel = channelIterator.next();
                if (socketChannel.read(byteBuffer) > 0) {
                    byteBuffer.flip();
                    byte[] by2 = new byte[byteBuffer.limit()];
                    byteBuffer.get(by2);
                    Loggers.SERVER_LOGGER.info(new String(by2));
                    socketChannel.write(ByteBuffer.wrap(by2));
                }
            }
        }
    }
}
