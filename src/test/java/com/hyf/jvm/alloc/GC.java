package com.hyf.jvm.alloc;

/**
 * @author baB_hyf
 * @date 2021/04/11
 */
public class GC {
    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0;
        System.gc();
    }
}
