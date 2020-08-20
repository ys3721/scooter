package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Yao Shuai
 * @date: 2020/8/17 15:34
 */
public class BIOWithThread {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9100);
        while (true) {
            final Socket connection = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        handlerRequest(connection);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                private void handlerRequest(Socket connection) throws Exception {
                    String line = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine() +"\n";
                    Loggers.SERVER_LOGGER.info("server receive :" + line);
                    //do some slowly
                    Thread.sleep(10000);
                    connection.getOutputStream().write(line.getBytes());
                    connection.getOutputStream().flush();
                    connection.close();
                }
            }).start();
        }

    }


}
