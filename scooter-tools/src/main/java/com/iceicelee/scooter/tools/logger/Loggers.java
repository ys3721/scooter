package com.iceicelee.scooter.tools.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author: Yao Shuai
 * @date: 2020/6/2 16:27
 */
public class Loggers {

    public static final Logger SERVER = LogManager.getLogger("scooter.reverse.server");

    public static final Logger CLIENT = LogManager.getLogger("scooter.reverse.client");

}
