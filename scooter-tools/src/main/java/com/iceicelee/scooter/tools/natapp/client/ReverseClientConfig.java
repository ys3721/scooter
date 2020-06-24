package com.iceicelee.scooter.tools.natapp.client;


import com.iceicelee.scooter.tools.natapp.ReverseProxyConstants;

/**
 * 一些配置信息
 *
 * @author: Yao Shuai
 * @date: 2020/6/19 14:23
 */
public class ReverseClientConfig {

    /**
     * 用于穿透的转发服务器的ip地址，它默认监听在{@link ReverseProxyConstants#CONSULT_PORT}端口
     */
    private final String reverserProxyIp;

    /**
     * 被代理的服务器的ip，因为到了内网之后，不仅仅可以连接localhost，也可以连接这个内网的其他机器
     * 这个值现在是启动服务器的时候传进来的，不可动态修改。
     */
    private final String targetLanIp;


    public ReverseClientConfig(String serverIp, String targetLanIp) {
        this.reverserProxyIp = serverIp;
        this.targetLanIp = targetLanIp;
    }

    public String getTargetLanIp() {
        return targetLanIp;
    }

    public String getReverserProxyIp() {
        return reverserProxyIp;
    }
}
