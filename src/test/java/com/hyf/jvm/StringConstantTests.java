package com.hyf.jvm;

import org.junit.Test;

/**
 * @author baB_hyf
 * @date 2021/04/03
 */
public class StringTests {

    private String s1 = "1"; // 常量池
    private static String s2 = "1"; // 常量池
    private static final String s3 = "1"; // 常量池

    private String s7 = new String("1"); // ?
    private static String s8 = new String("1"); //?
    private static final String s9 = new String("1"); // ?

    @Test
    public void test1() {
        String s4 = "1"; // 常量池
        String s5 = new String("1"); // 堆
        String s6 = new String("1").intern(); // 常量池

        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s2 == s3);

        System.out.println("==========");

        System.out.println(s1 == s4);
        System.out.println(s1 == s5);
        System.out.println(s4 == s5);
        System.out.println(s5 == s6);
        System.out.println(s4 == s6);

        System.out.println("==========");

        System.out.println(s1 == s7);
        System.out.println(s1 == s8);
        System.out.println(s1 == s9);
        System.out.println(s7 == s8);
        System.out.println(s7 == s9);
        System.out.println(s8 == s9);
    }
}
