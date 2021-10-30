package com.hyf.bytecode.bytebuddy;

import javassist.*;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author baB_hyf
 * @date 2021/10/29
 */
public class ByteBuddyEnhancer {

    private static boolean primitive = false;

    static {
        install();
        addDefaultClassPath();
    }

    public static Instrumentation getInstrumentation() {
        return ByteBuddyAgent.getInstrumentation();
    }

    public static void primitive(boolean primitive) {
        ByteBuddyEnhancer.primitive = primitive;
    }

    private static void install() {
        ByteBuddyAgent.install();
    }

    private static void addDefaultClassPath() {
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath ccp = new ClassClassPath(ByteBuddyEnhancer.class);
        pool.appendClassPath(ccp);
    }

    public static void addTransform(String regex) {
        getInstrumentation().addTransformer(new Transform(regex), true);
    }

    public static void reTransform(Class<?> clazz) throws Exception {
        reTransform(getInstrumentation(), clazz);
    }

    public static void reTransform(Instrumentation instrumentation, Class<?> clazz) throws Exception {
        try {
            clazz.getSuperclass().getConstructor(boolean.class);
        } catch (NoSuchMethodException e) {
            try {
                clazz.getSuperclass().getConstructor(Boolean.class);
            } catch (NoSuchMethodException ignore) {
                return;
            }
        }

        try {
            clazz.getConstructor(Boolean.class);
            return;
        } catch (NoSuchMethodException e) {
        }

        instrumentation.retransformClasses(clazz);
    }

    private static byte[] getClassByte(Class<?> clazz) throws Exception {
        return getClassByte(clazz.getName());
    }

    private static byte[] getClassByte(String className) throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass bClass = pool.get(className);

        if (primitive) {
            CtConstructor booleanConstructor = CtNewConstructor.make(new CtClass[]{CtClass.booleanType}, new CtClass[]{},
                    "super($1);", bClass);
            bClass.addConstructor(booleanConstructor);
        }
        else {
            CtClass BooleanClass = pool.get(Boolean.class.getName());
            CtConstructor constructor = CtNewConstructor.make(new CtClass[]{BooleanClass}, new CtClass[]{},
                    "super($1.booleanValue());", bClass);
            bClass.addConstructor(constructor);
        }

        // bClass.writeFile("E:\\aaa");
        return bClass.toBytecode();
    }

    public static class Transform implements ClassFileTransformer {

        private final String regex;

        public Transform(String regex) {
            this.regex = regex;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

            try {
                className = className.replace("/", ".");
                if (className.matches(regex)) {
                    return getClassByte(className);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return classfileBuffer;
        }
    }
}
