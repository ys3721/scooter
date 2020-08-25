package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: Yao Shuai
 * @date: 2020/8/25 18:07
 */
public class SocketClientTest {

    public static void main(String[] args) throws Exception {

        Selector selector = Selector.open();
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        socketChannel.connect(new InetSocketAddress(9100));

        while (true) {
            selector.select();
            for (Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext(); ) {
                SelectionKey _key = iterator.next();
                iterator.remove();
                if (_key.isReadable()) {
                    SocketChannel channel = (SocketChannel) _key.channel();
                    int _status = channel.read(byteBuffer);
                    if (_status < 0) {
                        channel.close();
                    }
                    byteBuffer.flip();
                    Loggers.REVERSE_CLIENT.info(new String(byteBuffer.array(), 0, byteBuffer.limit()));
                    byteBuffer.clear();
                } else if (_key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) _key.channel();

                    // Finish the connection. If the connection operation failed
                    // this will raise an IOException.
                    try {
                        channel.finishConnect();
                    } catch (IOException e) {
                        // Cancel the channel's registration with our selector
                        e.printStackTrace();
                        _key.cancel();
                        return;
                    }
                    // Register an interest in writing on this channel
                    _key.interestOps(SelectionKey.OP_WRITE);

                } else if (_key.isWritable()) {
                    _key.interestOps(SelectionKey.OP_READ);
                    SocketChannel channel = (SocketChannel) _key.channel();
                    channel.write(ByteBuffer.wrap("ok\n".getBytes()));

                }
            }

        }
    }
}
