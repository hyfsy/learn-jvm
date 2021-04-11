package com.hyf.jvm;

import org.junit.Test;

/**
 * @author baB_hyf
 * @date 2021/04/05
 */
public class EmptyBraceTests {

    @Test
    public void test1() {
        System.out.println(1);
        {
            System.out.println(2);
        }
        System.out.println(3);
    }
}
