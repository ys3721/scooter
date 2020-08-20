package com.iceicelee.scooter.gameserver.socket;

import com.iceicelee.scooter.gameserver.logger.Loggers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: Yao Shuai
 * @date: 2020/8/17 15:23
 */
public class BIOSocket {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(9100));
        while (true) {
            Socket socket = serverSocket.accept();
            Loggers.SERVER_LOGGER.info("Bio socket blocked by accept！");
            String line = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            Loggers.SERVER_LOGGER.info("Bio socket blocked by read！");
            socket.getOutputStream().write(line.getBytes());
            socket.getOutputStream().flush();
            socket.close();
        }
    }
}
