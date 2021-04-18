package com.hyf.jvm.cls.bytecode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2021/04/10
 */
public class Javap_2 {

    public static void main(String[] args) {

    }

    public void longValue(long value) {
        value++;
    }

    private void cannotSee() {
        // javap看不到，jclasslib插件可以看到
    }

    public void switchOrder() {
        switch ("a") {
            case "a":
                break;
            case "b":
                break;
            default:
                break;
        }
    }

    public void methodParameters(final String str) {
    }

    public void invokeDynamic() {
        DynamicInterface.ddd2();
        new DynamicInterface() {}.ddd();

        List<String> l = new ArrayList<>();
        l.forEach(System.out::println);
    }

    interface DynamicInterface {
        default void ddd() {
            System.out.println(1);
        }
        static void ddd2() {
            System.out.println(1);
        }
    }
}
