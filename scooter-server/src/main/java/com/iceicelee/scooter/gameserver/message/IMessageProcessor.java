package com.iceicelee.scooter.gameserver.message;

public interface IMessageProcessor {
	/**
	 * 判断队列是否已经达到上限了
	 * 
	 * @return
	 */
	public boolean isFull();

	/**
	 * 向消息队列投递消息
	 * 
	 * @param msg
	 */
	public void put(IMessage msg);

	/**
	 * 启动消息处理器
	 */
	public void start();

	/**
	 * 停止消息处理器
	 */
	public void stop();
}