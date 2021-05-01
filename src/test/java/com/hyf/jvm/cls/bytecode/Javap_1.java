package com.hyf.jvm.cls.bytecode;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * javac Javap_1.java
 * javap -verbose Javap_1.class
 */
@Deprecated
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class Javap_1<T> extends ArrayList<T> implements Serializable {

    private static final String              constant_string1 = "constant string1";
    private        final String              constant_string2 = "constant string2";
    private static       String              constant_string3 = "constant string3";
    private static final int                 constant_int1    = 1;
    private        final int                 constant_int2    = 2;
    private static       int                 constant_int3    = 3;
    private        final List<String>        constant_list    = new LinkedList<>();
    private              PrintStream         system           = System.out;

    static {
        System.out.println("static invoke");
    }

    {
        System.out.println("field invoke");
    }

    @Deprecated
    public static void main(String[] args) {
        System.out.println("Hello Javap!");

        final String s = "constant string";
        System.out.println("Hello " + s);

        String s2 = "Hello constant string";
        System.out.println(s2 == "Hello " + s);
    }

    public int[] index(int[] is, int i, char c, char[] cs, char c2, int i2, int i3, int[] is2) {
        return new int[128];
    }

    public strictfp void varargs(String... strs) throws Exception {
        try {
            System.out.println(Arrays.toString(strs));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("throw exception or normal executing");
        }
    }

    public static void noArgs() {
    }

    public void longLocalsStacks_Two(long l) {
        l = 1;
    }

    public InnerClass_D getD() {
        return new InnerClass_D();
    }

    private class InnerClass_D {
        public void getOuter() {
            System.out.println(this);
            System.out.println(Javap_1.this);
        }
    }

    private class InnerClass_E {
        public InnerClass_E ddd() {
            return this;
        }
    }

    public int inc() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }
}

class OuterClass_C {

}