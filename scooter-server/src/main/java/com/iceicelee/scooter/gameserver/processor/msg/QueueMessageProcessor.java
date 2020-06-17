package com.iceicelee.scooter.gameserver.processor.msg;

import com.iceicelee.scooter.gameserver.logger.Loggers;
import com.iceicelee.scooter.gameserver.message.IMessage;
import com.iceicelee.scooter.gameserver.message.SessionMessage;
import com.iceicelee.scooter.gameserver.processor.constants.CommonErrorLogInfo;
import com.iceicelee.scooter.gameserver.util.ErrorsUtil;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: Yao Shuai
 * @date: 2020/6/17 16:50
 */
public class QueueMessageProcessor implements Runnable {

    /** 消息队列 * */
    private final BlockingQueue<IMessage> queue;

    /** 消息处理器 * */
    private final IMessageHandler<IMessage> handler;

    /** 是否停止 */
    private volatile boolean stop = false;

    /** 处理的消息总数 */
    private long statisticsMessageCount = 0;

    private static final Logger queue_logger = Loggers.QUEUE_LOG;

    private static final Logger logger = Loggers.GAME_LOGGER;

    public QueueMessageProcessor(IMessageHandler<IMessage> messageHandler) {
        queue = new LinkedBlockingQueue<IMessage>();
        handler = messageHandler;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                process(queue.take());
                if (queue_logger.isDebugEnabled()) {
                    queue_logger.debug("run queue size:" + queue.size());
                }
            } catch (InterruptedException e) {
                if (logger.isWarnEnabled()) {
                    logger.warn("[#CORE.QueueMessageProcessor.run] [Stop process]");
                }
                break;
            } catch (Exception e) {
                logger.error("MSG.PRO.ERR.EXP" + "QueueMessageProcessor:" + e.toString());
            }
        }
    }

    /**
     * 处理具体的消息，每个消息有自己的参数和来源,如果在处理消息的过程中发生异常,则马上将此消息的发送者断掉
     *
     * @param msg
     */
    public void process(IMessage msg) {
        if (msg == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("[#CORE.QueueMessageProcessor.process] ["
                        + CommonErrorLogInfo.MSG_PRO_ERR_NULL_MSG + "]");
            }
            return;
        }
        long begin = 0;
        if (logger.isInfoEnabled()) {
            begin = System.nanoTime();
        }
        this.statisticsMessageCount++;
        try {
            this.handler.execute(msg);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(ErrorsUtil.error("Error",
                        "#.QueueMessageProcessor.process", "param"), e);
            }
            try {
                // TODO 此处的逻辑应该由一个接口代为实现
                if (msg instanceof SessionMessage<?>) {
                    // 如果在处理消息的过程中出现了错误,将其断掉
                    final SessionMessage<?> imsg = (SessionMessage<?>) msg;
                    if (imsg.getSession() != null) {
                        if (logger.isErrorEnabled()) {
                            logger.error(ErrorsUtil.error(
                                    CommonErrorLogInfo.MSG_PRO_ERR_EXP,
                                    "Disconnect sender", msg.getTypeName()
                                            + "@" + imsg.getSession()), e);
                        }
                        imsg.getSession().close();
                    }
                }
            } catch (Exception ex) {
                logger.error(ErrorsUtil.error(
                        CommonErrorLogInfo.MSG_PRO_ERR_DIS_SENDER_FAIL,
                        "#CORE.QueueMessageProcessor.process", null), ex);
            }
        } finally {
            if (logger.isInfoEnabled()) {
                // 特例，统计时间跨度
                long time = (System.nanoTime() - begin) / (1000 * 1000);
                if (time > 1) {
                    int type = msg.getType();
                    logger.info("Message:" + msg.getTypeName() + " Type:"
                            + type + " Time:" + time + "ms" + " Total:"
                            + this.statisticsMessageCount);
                }

                if (time > 10) {
                    int type = msg.getType();
                    logger.info("Message:" + msg.getTypeName() + " Type:"
                            + type + " Time:" + time + "ms" + " Total:"
                            + this.statisticsMessageCount);
                }
            }
        }
    }
}
