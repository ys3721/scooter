package com.iceicelee.scooter.gameserver;

import com.iceicelee.scooter.gameserver.message.IMessageProcessor;
import com.iceicelee.scooter.gameserver.message.IMessageRecognizer;

/**
 * @author: Yao Shuai
 * @date: 2020/6/17 18:19
 */
public class ProcessEngine {

    private int port;

    private boolean useSsl;

    private final IMessageRecognizer messageRecognizer;

    private final IMessageProcessor messageProcessor = null;


    public ProcessEngine(int port, boolean useSsl, IMessageRecognizer messageRecognizer) {
        this.port = port;
        this.useSsl = useSsl;
        this.messageRecognizer = messageRecognizer;
    }

}
