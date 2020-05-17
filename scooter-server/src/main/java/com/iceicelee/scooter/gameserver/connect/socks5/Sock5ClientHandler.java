package com.iceicelee.scooter.gameserver.connect.socks5;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;

/**
 * @author: Yao Shuai
 * @date: 2020/5/12 12:32
 */
public class Sock5ClientHandler extends ChannelInboundHandlerAdapter {

    private boolean methodSelected;

    private boolean connected;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf byteBuf = ctx.alloc().buffer(3);
        byteBuf.writeByte(5);
        byteBuf.writeByte(1);
        byteBuf.writeByte(0);
        ctx.writeAndFlush(byteBuf);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (!this.methodSelected) {
            ByteBuf m = (ByteBuf) msg;
            System.err.println("Socket" + m.readByte() +" m= " + m.readByte() + " Method is selected!!");
            m.release();
            this.methodSelected = true;
            ByteBuf writeBuf = ctx.alloc().buffer();
            writeBuf.writeByte(5);
            writeBuf.writeByte(1);
            writeBuf.writeByte(0);
            writeBuf.writeByte(1);
            //ip
            writeBuf.writeByte(172);
            writeBuf.writeByte(105);
            writeBuf.writeByte(231);
            writeBuf.writeByte(66);
            //端口
            writeBuf.writeShort(80);
            ctx.writeAndFlush(writeBuf);
        } else if (!connected) {
            ByteBuf m = (ByteBuf) msg;
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            //ip
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            System.out.println(m.readByte());
            System.out.println(m.readShort());
            //
            this.connected = true;
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ba)));
            writer.println("GET /index.html HTTP/1.1");
            writer.println("Host: 172.105.231.66:80");
            writer.println("Connection: Close");
            writer.println();
            writer.flush();
            byte[] req = ba.toByteArray();
            ByteBuf br = ctx.alloc().buffer(req.length);
            br.writeBytes(req);
            ctx.writeAndFlush(br);
        } else {
            System.err.println("收到了");
            ByteBuf m = (ByteBuf) msg;
            for (int i = 0; i < m.readableBytes(); i++) {
                System.out.print((char)m.getByte(i));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            sendNow(ctx);
        }
    }

    private void sendNow(ChannelHandlerContext ctx) {
        System.out.println("==============================");
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(ba)));
        writer.println("GET /index.html HTTP/1.1");
        writer.println("Host: 172.105.231.66:80");
        writer.println("Connection: Close");
        writer.println();
        writer.flush();
        byte[] req = ba.toByteArray();
        ByteBuf br = ctx.alloc().buffer(req.length);
        br.writeBytes(req);
        ctx.writeAndFlush(br);
    }
}
