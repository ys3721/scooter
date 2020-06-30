package com.iceicelee.scooter.tools.natapp.client;

import com.iceicelee.scooter.tools.natapp.client.config.ReverseClientConfig;
import com.iceicelee.scooter.tools.natapp.client.consult.ClientConsultService;

/**
 * @author: Yao Shuai
 * @date: 2020/6/19 14:22
 */
public class ClientContext {

    /**
     * 一些参数配置等等
     */
    private static ReverseClientConfig config;

    /**
     * 用于和proxy通讯的服务
     *
     */
    private static ClientConsultService consultService;


    public static void initContext(ReverseClientConfig config) {
        ClientContext.config = config;
        consultService = new ClientConsultService(config.getRemoteServerIp(), config.getRemoteServerPort());
    }

    public ReverseClientConfig getConfig() {
        return config;
    }

    public static ClientConsultService getConsultService() {
        return consultService;
    }


}
