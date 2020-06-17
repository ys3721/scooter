package com.iceicelee.scooter.gameserver.message;

/**
 * 封装会话的业务逻辑
 * 
 * 
 */
public interface ISession {

	/**
	 * 
	 */
	public void close();

	/**
	 * 出现异常时是否关闭连接
	 * 
	 * @return
	 */
	public boolean closeOnException();

	/**
	 * 判断当前会话是否处于连接状态
	 * 
	 * @return
	 */
	public boolean isConnected();

	/**
	 * @param msg
	 */
	public void write(IMessage msg);
}
