package com.hyf.jvm.execute.dispatch;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * 控制方法分派
 *
 * @author baB_hyf
 * @date 2021/04/24
 */
public class Control_Dispatch {
    static class GrandFather {
        void thinking() {
            System.out.println("i am grandfather");
        }
    }

    static class Father extends GrandFather {
        @Override
        void thinking() {
            System.out.println("i am father");
        }
    }

    static class Son extends Father {
        // 实现调用祖父类的thinking()方法，打印"i am grandfather"
        @Override
        void thinking() {
            // 使用JDK 7 Update 9之前的HotSpot虚拟机运行
            // try {
            //     MethodType mt = MethodType.methodType(void.class);
            //     MethodHandle mh = lookup().findSpecial(GrandFather.class,
            //             "thinking", mt, getClass());
            //     mh.invoke(this);
            // } catch (Throwable e) {
            // }

            try {
                MethodType mt = MethodType.methodType(void.class);
                Field lookupImpl = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
                lookupImpl.setAccessible(true);
                MethodHandle mh = ((MethodHandles.Lookup) lookupImpl.get(null)).findSpecial(GrandFather.class,"thinking", mt, GrandFather.class);
                mh.invoke(this);
            } catch (Throwable e) {
            }
        }
    }

    public static void main(String[] args) {
        new Son().thinking();
    }
}
