package com.iceicelee.scooter.tools.natapp.client.consult;

import com.google.protobuf.MessageLite;
import com.iceicelee.scooter.tools.logger.Loggers;
import com.iceicelee.scooter.tools.natapp.client.config.ProtoMessageRecogenazer;
import com.iceicelee.scooter.tools.natapp.client.processor.SCNoticeSomeOneConnectedMsgProcessorFactory;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto.CSHandshakeMsg;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto.SCNoticeSomeOneConnectedMsg;
import com.iceicelee.scooter.tools.natapp.server.processor.CSHandshakeMsgProcessorFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 用于协商的handler
 *
 * @author: Yao Shuai
 * @date: 2020/6/3 18:23
 */
public class ClientConsultHandler extends SimpleChannelInboundHandler<MessageLite> {

    private String consultIp;

    private int port;

    public ClientConsultHandler(String consultIp) {
        this.consultIp = consultIp;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        CSHandshakeMsg csHandshakeMsg = CSHandshakeMsg.newBuilder()
                .setClientInfo("Client no infos").build();
        ctx.writeAndFlush(csHandshakeMsg);
        Loggers.REVERSE_CLIENT.info("channelActive handshake msg is seed! 与远端服务已经链接！");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageLite msg) throws Exception {
        MessageLite messageLite = (MessageLite) msg;
        //先这么写八
        switch (ProtoMessageRecogenazer.getMessageNum(messageLite)) {
            case 1003:
                SCNoticeSomeOneConnectedMsgProcessorFactory.getProcessor().process((SCNoticeSomeOneConnectedMsg) msg);
                break;
            default:
                System.out.println("No processor for " + msg);
        }
    }

  /*  @Override
    protected void channelRead0(ChannelHandlerContext ctx, SCMessage msg) throws Exception {

        *//*Loggers.REVERSE_CLIENT.debug("收到了来自服务端的消息->" + msg);
        byte[] scConsultMsgByte =  new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(scConsultMsgByte);
        SCHandshakeMsg scHandshakeMsg = SCHandshakeMsg.parseFrom(scConsultMsgByte);
        Loggers.REVERSE_CLIENT.info("Server want me connection port " + scHandshakeMsg.getHandshakeResult());
        String[] ports = scHandshakeMsg.getHandshakeResult().split(",");
        this.port = Integer.parseInt(ports[0]);
        int heWantPort = Integer.parseInt(ports[1]);
        Loggers.REVERSE_CLIENT.info("Begin connect the 127.0.0.1 app port " + heWantPort);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                EventLoopGroup workGroup = new NioEventLoopGroup();
                ClientAppLocalHandler appLocalHandler = new ClientAppLocalHandler();
                try {
                    Bootstrap b = new Bootstrap();
                    b.group(workGroup)
                            .channel(NioSocketChannel.class)
                            .handler(new LoggingHandler())
                            .handler(appLocalHandler);
                    ChannelFuture connFuture = b.connect("10.5.7.163", heWantPort);
                    connFuture.sync();
                    Channel appChannel = connFuture.channel();
                    Loggers.REVERSE_CLIENT.info("Connected the 127.0.0.1 app port " + heWantPort);

                    EventLoopGroup workGroup2 = new NioEventLoopGroup();
                    Bootstrap cb = new Bootstrap();
                    cb.group(workGroup2)
                            .channel(NioSocketChannel.class)
                            .handler(new LoggingHandler())
                            .handler(new ClientConmmunicationHandler(appChannel, appLocalHandler));
                    ChannelFuture connFuture2 = cb.connect(ClientConsultHandler.this.getConsultIp(), ClientConsultHandler.this.getPort());
                    connFuture2.sync();
                    Loggers.REVERSE_CLIENT.info(connFuture2.isSuccess()+"Back Connected the "+ClientConsultHandler.this.getConsultIp() +" port " + ClientConsultHandler.this.getPort());
                } catch (Exception e) {
                    Loggers.REVERSE_CLIENT.error("链接失败！", e);
                }
            }
        });
        thread.start();*//*
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

    public String getConsultIp() {
        return consultIp;
    }

    public void setConsultIp(String consultIp) {
        this.consultIp = consultIp;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


}
