package com.hyf.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2021/04/09
 */
public class WatchTests {

    public static void main(String[] args) {
        List<Integer> memoryList = new ArrayList<>();
        int i = 0;
        try {
            while (true) {
                memoryList.add(i++);
                Thread.sleep(100);
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(memoryList.get(0));
    }
}
