package com.hyf.jvm.cls.bytecode;

import org.junit.Test;

/**
 * @author baB_hyf
 * @date 2021/04/10
 */
public class Instruct_1 {

    @Test
    public void test1() {
        System.out.println(inc());
    }
    public int inc() {
        int x;
        try {
            x = 1;
            // throw new RuntimeException();
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3; // !!!
        }
    }

    @Test
    public void test2() {
        int i = 1;
        float f = 1.0f;
        double d = 1.0d;
        String s = "1";
        System.out.println(1 == i); // if_icmpne
        System.out.println(1.0f == f); // fcmpl
        System.out.println(1.0d == d); // dcmpl
        System.out.println("1" == s); // if_acmpne
    }

    @Test
    public void test3() {
        System.out.println(1F / 0); // not string
        System.out.println(1D / 0); // not string
        // System.out.println(1L / 0); // error
        // System.out.println(1 % 0); // error

        double d = 1D / 0;
        int i = (int)d;
        System.out.println(i);
        System.out.println((double)i);
    }
}
