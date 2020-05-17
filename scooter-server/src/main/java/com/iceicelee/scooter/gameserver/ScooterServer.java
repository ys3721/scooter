package com.iceicelee.scooter.gameserver;

import com.iceicelee.scooter.gameserver.connect.Connector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author: Yao Shuai
 * @date: 2020/4/26 21:19
 */
public class ScooterServer {

	private static final Logger LOGGER = LogManager.getLogger(ScooterServer.class);

	public static void main(String[] args) {
		System.out.println("ScooterServer begin!");
		String thing = args.length > 0 ? args[0] : "world";
		LOGGER.info("Hello, {}!", thing);
		LOGGER.debug("Got calculated value only if debug enabled: {}", () -> doSomeCalculation());

		ScooterServer scooter = new ScooterServer();
		scooter.beginConnector();
	}

	private static Object doSomeCalculation() {
		return "yao shuai";
	}

	private void beginConnector() {
		LOGGER.error("Got calculated value only if debug enabled: {}", () -> doSomeCalculation());
		Connector connector = new Connector();
	}


}