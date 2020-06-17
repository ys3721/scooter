package com.iceicelee.scooter.gameserver;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import com.iceicelee.scooter.gameserver.processor.ServerMessageProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author: Yao Shuai
 * @date: 2020/4/26 21:19
 */
public class ScooterServer {


	/** 服务器进程 */
	private ServerMessageProcessor serverProcess;

	private static final Logger LOGGER = Loggers.GAME_LOGGER;

	public static void main(String[] args) {
		LOGGER.info("ScooterServer begin start! good luck!");
		ScooterServer scooter = new ScooterServer();
		scooter.ignite();
		scooter.launch();
	}

	private void ignite() {
		serverProcess = new ServerMessageProcessor();
	}

	private void launch() {

	}

	private static Object doSomeCalculation() {
		return "yao shuai";
	}

}