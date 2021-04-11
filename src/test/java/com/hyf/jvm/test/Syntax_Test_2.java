package com.hyf.jvm.test;

/**
 * @author baB_hyf
 * @date 2021/04/11
 */
public class T2 {

    static {
        i = 0; // 给变量赋值可以正常编译通过
        // System.out.print(i); // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;

    public static void main(String[] args) {
        // ResourceBundle.getBundle("dd");
    }
}
