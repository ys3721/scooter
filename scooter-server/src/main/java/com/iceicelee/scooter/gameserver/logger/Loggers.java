package com.iceicelee.scooter.gameserver.logger;

import com.iceicelee.scooter.gameserver.ScooterServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author: Yao Shuai
 * @date: 2020/6/2 16:27
 */
public class Loggers {

    public static final Logger SERVER_LOGGER = LogManager.getLogger("scooter.reverse.server");

    public static final Logger REVERSE_CLIENT = LogManager.getLogger("scooter.reverse.client");

    public static final Logger ECHO = LogManager.getLogger("scooter.echo");

    public static final  Logger GAME_LOGGER = LogManager.getLogger("scooter.gameserver");

    public static final  Logger MSG_LOGGER = LogManager.getLogger("scooter.msg");

    public static final  Logger QUEUE_LOG = LogManager.getLogger("scooter.queue");

}
