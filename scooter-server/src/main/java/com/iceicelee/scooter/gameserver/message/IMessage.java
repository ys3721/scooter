package com.iceicelee.scooter.gameserver.message;

/**
 * @author: Yao Shuai
 * @date: 2020/6/17 16:52
 */
public interface IMessage {

    /**
     *
     * @return
     */
    String getTypeName();

    /**
     *
     * @return
     */
    int getType();

    /**
     *
     */
    void execute();
}
