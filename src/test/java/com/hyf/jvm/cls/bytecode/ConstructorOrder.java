package com.hyf.jvm.cls.bytecode;

/**
 * @author baB_hyf
 * @date 2021/04/27
 */
public class ConstructorOrder {

    private static String s1 = "static 1";

    static {
        System.out.println("static 2");
    }

    private static String s2 = "static 3";

    {
        System.out.println("obj 1");
    }

    public ConstructorOrder() {
        System.out.println("obj 2");
    }

    {
        System.out.println("obj 3");
    }

    static {
        System.out.println("static 4");
    }

    static {
        System.out.println("obj 4");
    }

    public static void main(String[] args) {
        new PrivateA();
    }

}
