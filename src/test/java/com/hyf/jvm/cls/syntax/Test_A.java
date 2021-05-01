package com.hyf.jvm.cls.syntax;

/**
 * @author baB_hyf
 * @date 2021/04/14
 */
public class Test_A {

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));

        System.setIn(null);
        System.setOut(null);

        int[][][][][] array = new int[1][0][1][1][1];
        // int[][][] array2 = new int[1][0][-1];
    }
}
