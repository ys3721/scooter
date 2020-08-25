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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 这个地方没有想好，boss event loop 用来select事件然后  worker线程用来读取？
 * 明天在看这个吧 netty 是不是这么搞的？
 *
 * 还是实现一个echo，ECHO也算是个协议哇。 没有协议的瞎写真是咋写都是逆风.
 *
 * @author: Yao Shuai
 * @date: 2020/8/19 17:10
 */
public class NIOSelectorWhitMultipleThread {

    private ServerSocketChannel serverSocketChannel;
    private Selector acceptSelector;

    private Selector readSelector1;
    private Selector readSelector2;

    public static void main(String[] args) throws IOException {
        NIOSelectorWhitMultipleThread multipleThread = new NIOSelectorWhitMultipleThread();
        multipleThread.initServer();
        multipleThread.startThread();
    }

    private void startThread() {
        MockThread acThread = new MockThread(acceptSelector, 2);
        acThread.setName("a1");
        MockThread readThread1 = new MockThread(readSelector1);
        readThread1.setName("r1");
        MockThread readThread2 = new MockThread(readSelector2);
        readThread2.setName("r2");

        acThread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        readThread1.start();
        readThread2.start();

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initServer() throws IOException {
        acceptSelector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(9100));

        readSelector1 = Selector.open();
        readSelector2 = Selector.open();

        serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
    }
}

class MockThread extends Thread {
    Selector selector = null;
    static int selectors = 0;

    private boolean boss = false;

    int id = 0;

    static BlockingQueue<SocketChannel>[] queues;

    static AtomicInteger index = new AtomicInteger();

    MockThread(Selector selector, int n) {
        this.selector = selector;
        queues = new LinkedBlockingQueue[n];
        for (int i = 0; i < n; i++) {
            queues[i] = new LinkedBlockingQueue<>();
        }
        selectors = n;
        boss = true;
    }

    MockThread(Selector selector) {
        this.selector = selector;
        id = index.getAndIncrement() % selectors;
    }

    @Override
    public void run() {
        while (true) {
            try {
                while (selector.select(1000) > 0) {
                    for(Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        keyIterator.hasNext(); ) {
                        SelectionKey selectionKey = keyIterator.next();
                        keyIterator.remove();
                        if (selectionKey.isAcceptable()) {
                            acceptHandler(selectionKey);
                        } else if (selectionKey.isReadable()) {
                           readHandler(selectionKey);
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (!queues[id].isEmpty() && !boss) {
                try {
                    SocketChannel clientChannel = queues[id].take();
                    clientChannel.configureBlocking(false);
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    clientChannel.register(selector, SelectionKey.OP_READ, byteBuffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readHandler(SelectionKey selectionKey) {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
        try {
            if( -1 == channel.read(byteBuffer)) {
                channel.close();
            }
            byteBuffer.flip();
            Loggers.SERVER_LOGGER.info("TS->[RECEIVE]:" + new String(byteBuffer.array(), 0 , byteBuffer.limit()));
            channel.write(byteBuffer);
            byteBuffer.clear();
        } catch (IOException e) {
            try {
                channel.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void acceptHandler(SelectionKey selectionKey) throws IOException {
        int num = index.getAndIncrement()% selectors;
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        queues[num].add(client);
    }

}
