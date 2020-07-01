package com.iceicelee.scooter.tools.natapp.server.processor;

import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;

public class CSHandshakeMsgProcessor {

    public void process(ConsultMessageProto.CSHandshakeMsg csHandshakeMsg) {
        Loggers.REVERSE_LOGGER.info("处理 " + csHandshakeMsg);
    }

}
