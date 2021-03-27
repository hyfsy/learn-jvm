package com.hyf.jvm.hello;

/**
 * @author baB_hyf
 * @date 2021/03/27
 */
public class T {

    public static void main(String[] args) {
        System.out.println(1);
    }


    static class A {

        static {
            System.out.println(2);
        }
        {
            System.out.println(1);
        }
    }
}
