package com.hyf.bytecode.javassist.hello;

import javassist.ClassPool;
import javassist.CtClass;

/**
 * @author baB_hyf
 * @date 2021/05/03
 */
public class Hello {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("com.hyf.bytecode.javassist.hello.Child");
        cc.setSuperclass(pool.get("com.hyf.bytecode.javassist.hello.Super"));
        cc.writeFile("target/classes");
        Class aClass = cc.toClass(); // 直接转换为Class对象
        System.out.println(aClass);

        Object o = aClass.newInstance();
        System.out.println(o instanceof Super); // 运行时修改类的字节码
        System.out.println(o instanceof Child);
    }
}
