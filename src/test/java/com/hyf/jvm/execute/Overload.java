package com.hyf.jvm.execute;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author baB_hyf
 * @date 2021/04/24
 */
public class Overload {
    public static void sayHello(Object arg) {
        System.out.println("hello Object");
    }
    public static void sayHello(int arg) {
        System.out.println("hello int");
    }
    public static void sayHello(long arg) {
        System.out.println("hello long");
    }
    public static void sayHello(Character arg) {
        System.out.println("hello Character");
    }
    public static void sayHello(char arg) {
        System.out.println("hello char");
    }
    public static void sayHello(char... arg) {
        System.out.println("hello char ...");
    }
    public static void sayHello(Serializable arg) {
        System.out.println("hello Serializable");
    }
    // public static void sayHello(Comparable arg) {
    //     System.out.println("hello Comparable");
    // }
    public static void main(String[] args) {
        sayHello('a');
    }
}
