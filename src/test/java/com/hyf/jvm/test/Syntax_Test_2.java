package com.hyf.jvm.test;

/**
 * @author baB_hyf
 * @date 2021/04/11
 */
public class Syntax_Test_2 {

    static {
        i = 0; // 给变量赋值可以正常编译通过
        // System.out.print(i); // 这句编译器会提示“非法向前引用”
    }
    static int i = 1;

    public static void main(String[] args) {
        // ResourceBundle.getBundle("dd");
        System.out.println(i);
        A.invoke();
        B.invoke();

        System.out.println(1024 >> 2 * 3);
    }

    static class A {
        public static void invoke() {
            System.out.println("A");
        }
    }

    static class B extends A {

        public static void invoke() { // static 与类型关联
            System.out.println("B");
        }
    }
}
