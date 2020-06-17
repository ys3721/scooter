package com.iceicelee.scooter.gameserver.message;

import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Yao Shuai
 *
 */
public class BaseIoHandler extends ChannelInboundHandlerAdapter {

	protected IMessageProcessor msgProcessor;

	/**
	 * 设置MessageProcessor
	 * 
	 * @param msgProcessor
	 */
	public void setMessageProcessor(IMessageProcessor msgProcessor) {
		this.msgProcessor = msgProcessor;
	}
}