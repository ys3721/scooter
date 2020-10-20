package com.iceicelee.scooter.hotspot;

import java.util.LinkedList;

/**
 * @author: Yao Shuai
 * @date: 2020/9/24 20:50
 */
public class HotspotMemoryUseTest {

    public static void main(String[] args) throws Exception {
        LinkedList<Byte[]> l = new LinkedList<>();
        while (true) {
            l.add(new Byte[1024 * 1024]);
            Thread.sleep(10);
        }
    }
}
