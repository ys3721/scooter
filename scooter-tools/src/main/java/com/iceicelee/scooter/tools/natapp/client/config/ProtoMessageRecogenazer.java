package com.iceicelee.scooter.tools.natapp.client.config;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.MessageLite;
import com.iceicelee.scooter.tools.natapp.message.ConsultMessageProto;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Yao Shuai
 * @date: 2020/6/30 12:54
 */
public class ProtoMessageRecogenazer {

    private static Map<Integer, Class<? extends MessageLite>> msgs = new HashMap<>();

    private static Map<Integer, MessageLite> id2DefaultMsg = new HashMap<>();

    private static Map<Class<? extends MessageLite>, Integer> msg2Nums = new HashMap<>();


    static {
        Class<?>[] c =  ConsultMessageProto.class.getDeclaredClasses();
        for (Class clazz : c) {
            if (!com.google.protobuf.GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                continue;
            }
            Integer msgNumber = parseMessageNum(clazz);
            msgs.put(msgNumber, clazz);
            try {
                id2DefaultMsg.put(msgNumber, (MessageLite)clazz.getDeclaredMethod("getDefaultInstance").invoke(null));
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        for (Map.Entry<Integer, Class<? extends MessageLite>> entry : msgs.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
            msg2Nums.put(entry.getValue(), entry.getKey());
        }

        for (Map.Entry<Integer, MessageLite> entry : id2DefaultMsg.entrySet()) {
            System.out.println(entry.getKey() + " ====> " + entry.getValue().getParserForType());
        }
    }

    public static void main(String[] args) throws Exception {
        Class<?>[] c =  ConsultMessageProto.class.getDeclaredClasses();
        for (Class clazz : c) {
            if (!com.google.protobuf.GeneratedMessageV3.class.isAssignableFrom(clazz)) {
                continue;
            }
            Integer msgNumber = parseMessageNum(clazz);
            msgs.put(msgNumber, clazz);
            id2DefaultMsg.put(msgNumber, (MessageLite)clazz.getDeclaredMethod("getDefaultInstance").invoke(null));
        }
        for (Map.Entry<Integer, Class<? extends MessageLite>> entry : msgs.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
            msg2Nums.put(entry.getValue(), entry.getKey());
        }

        for (Map.Entry<Integer, MessageLite> entry : id2DefaultMsg.entrySet()) {
            System.out.println(entry.getKey() + " ====> " + entry.getValue().getParserForType());
        }
    }

    private static Integer parseMessageNum(Class clazz) {
        String name = clazz.getSimpleName();
        for(Descriptor descriptor : ConsultMessageProto.getDescriptor().getMessageTypes()) {
            if (descriptor.getName().equals(name)) {
                return descriptor.getOptions().getExtension(ConsultMessageProto.msgNumber);
            }
        }
        throw new RuntimeException("没有找到消息" + clazz);
    }

    public static Class<? extends MessageLite> getClassById(int id) {
        return msgs.get(id);
    }

    public static MessageLite getDefaultLiteById(int id) {
        return id2DefaultMsg.get(id);
    }

    public static int getMessageNum(MessageLite messageLite) {
        return msg2Nums.get(messageLite.getClass());
    }

}
