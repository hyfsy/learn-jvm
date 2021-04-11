package com.hyf.jvm.test;

/**
 * @author baB_hyf
 * @date 2021/04/10
 */
public class T1 {

    public static void main(String[] args) {
        System.out.println();
        // new C().s;
    }

    static class C extends F implements E {
        // private String s = "c";
    }

    static class F {
        final String s = "f";
    }

    interface E {
        String s = "e";

        // static {
        //
        // }

        default void a() {
        }

        static void b() {
        }

        // jdk9
        // private static void c() {
        // }
    }
}