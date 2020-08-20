package com.iceicelee.scooter.gameserver.processor;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import com.iceicelee.scooter.gameserver.message.IMessage;
import com.iceicelee.scooter.gameserver.processor.constants.CommonErrorLogInfo;
import com.iceicelee.scooter.gameserver.processor.msg.IMessageHandler;
import com.iceicelee.scooter.gameserver.processor.msg.QueueMessageProcessor;
import org.apache.logging.log4j.Logger;

/**
 * @author: Yao Shuai
 * @date: 2020/6/17 16:00
 */
public class ServerMessageProcessor {

    private static final Logger logger = Loggers.MSG_LOGGER;

    private QueueMessageProcessor mainMessageProcessor;


    public ServerMessageProcessor() {
        this.mainMessageProcessor = new QueueMessageProcessor(new IMessageHandler<IMessage>() {
            @Override
            public void execute(IMessage message) {
                try {
                    message.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(CommonErrorLogInfo.MSG_PRO_ERR_EXP
                            + "TypeName:" + message.getTypeName() + "|Type:"
                            + message.getType() + "|" + message.toString()
                            + e.toString());
                }
            }

            @Override
            public short[] getTypes() {
                return new short[0];
            }
        });
    }

    public QueueMessageProcessor getMainMessageProcessor() {
        return mainMessageProcessor;
    }

    public void setMainMessageProcessor(QueueMessageProcessor mainMessageProcessor) {
        this.mainMessageProcessor = mainMessageProcessor;
    }

    /**
     * 开启网络的监听
     *
     */
    public void start() {
        Loggers.SERVER_LOGGER.info("Will start net work ...");


    }
}
