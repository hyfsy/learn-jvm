package com.hyf.jvm.cls.loading;

/**
 * @author baB_hyf
 * @date 2021/04/10
 */
public class ClsLoading {
    /**
     * 非主动使用类字段演示
     **/
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }

    /**
     * 被动使用类字段演示一：
     * 通过子类引用父类的静态字段， 不会导致子类初始化
     **/
    static class SuperClass {

        public static int value = 123;

        static {
            System.out.println("SuperClass init!");
        }
    }

    static class SubClass extends SuperClass {
        static {
            System.out.println("SubClass init!");
        }
    }
}
