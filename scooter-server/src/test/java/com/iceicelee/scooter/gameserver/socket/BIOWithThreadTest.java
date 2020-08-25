package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import org.apache.logging.log4j.core.util.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

/**
 * @author: Yao Shuai
 * @date: 2020/8/17 16:40
 */
public class BIOWithThreadTest {

    @Test
    public void testBIOWithThread() throws IOException, InterruptedException {
        for (int i = 0; i < 1; i++) {
            final int no = i;
            Thread tast = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket socket = new Socket();
                    try {
                        socket.connect(new InetSocketAddress(9100));
                        socket.getOutputStream().write(("The client message"  + no +"\n").getBytes());
                        String readLine = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
                        Loggers.REVERSE_CLIENT.info(readLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            tast.setDaemon(false);
            tast.start();
        }
        assertEquals(1, 1);
        Thread.sleep(2000);
    }
}
