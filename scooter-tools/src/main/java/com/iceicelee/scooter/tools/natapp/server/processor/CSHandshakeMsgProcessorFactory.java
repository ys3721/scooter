package com.iceicelee.scooter.tools.natapp.server.processor;


public class CSHandshakeMsgProcessorFactory {

    private static CSHandshakeMsgProcessor processor = new CSHandshakeMsgProcessor();

    public static CSHandshakeMsgProcessor getProcessor() {
        return processor;
    }
}
