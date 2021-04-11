package com.hyf.jvm.alloc;

/**
 * 2. 大对象直接进入老年代
 * <p>
 * VM参数: -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:PretenureSizeThreshold=3145728
 *
 * @author baB_hyf
 * @date 2021/04/08
 */
public class Old {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] allocation;
        allocation = new byte[4 * _1MB]; // 直接分配在老年代中
    }
}
