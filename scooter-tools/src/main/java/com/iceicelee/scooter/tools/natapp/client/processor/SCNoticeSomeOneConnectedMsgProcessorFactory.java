package com.iceicelee.scooter.tools.natapp.client.processor;

/**
 * @author: Yao Shuai
 * @date: 2020/7/2 17:00
 */
public class SCNoticeSomeOneConnectedMsgProcessorFactory {

    private static final SCNoticeSomeOneConnectedMsgProcessor processor = new SCNoticeSomeOneConnectedMsgProcessor();

    public static SCNoticeSomeOneConnectedMsgProcessor getProcessor() {
        return processor;
    }

}
