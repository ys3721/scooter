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
                while (selector.select() > 0) {
                    for(Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                        keyIterator.hasNext(); ) {
                        SelectionKey selectionKey = keyIterator.next();
                        keyIterator.remove();
                        if (selectionKey.isAcceptable()) {
                            acceptHander(selectionKey);
                        } else if (selectionKey.isReadable()) {
                            readHandler(selectionKey);
                        }
                    }
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }
    }

    private void readHandler(SelectionKey selectionKey) {
    }

    private void acceptHander(SelectionKey selectionKey) {
        
    }


}
