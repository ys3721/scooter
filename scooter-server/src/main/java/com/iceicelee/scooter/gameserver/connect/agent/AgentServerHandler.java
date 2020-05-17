package com.iceicelee.scooter.gameserver.connect.agent;

import com.iceicelee.scooter.gameserver.connect.discard.TimeClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author: Yao Shuai
 * @date: 2020/5/12 15:37
 */
public class AgentServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        EventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap(); //(1)
        b.group(worker);//(2)
        b.channel(NioSocketChannel.class);//(3)
        b.option(ChannelOption.SO_KEEPALIVE,true);//(4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                });
            }
        });
        // 启动这个客户端
        ChannelFuture f = b.connect("127.0.0.1", 1080).sync();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { //(2)
        ((ByteBuf)msg).release();

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//(4)
        cause.printStackTrace();
        ctx.close();
    }

}
