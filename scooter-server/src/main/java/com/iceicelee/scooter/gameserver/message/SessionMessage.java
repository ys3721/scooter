package com.iceicelee.scooter.gameserver.message;

public interface SessionMessage<T extends ISession> extends IMessage {
	/**
	 * 取得此消息的发送者
	 * 
	 * @return
	 */
	public T getSession();

	/**
	 * 设置该消息的发送者
	 * 
	 * @param session
	 */
	public void setSession(T session);
}
