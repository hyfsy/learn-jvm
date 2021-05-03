package com.hyf.bytecode.javassist;

import javassist.*;
import org.junit.Test;

import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.ProtectionDomain;

/**
 * @author baB_hyf
 * @date 2021/05/03
 */
public class _3_ClassLoaderTests {

    private static final String PERSON_CLASS = "com.hyf.bytecode.javassist._3_ClassLoaderTests$Person";

    @Test
    public void testInvoke() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(PERSON_CLASS);
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        Class c = cc.toClass();
        System.out.println(c);

        System.out.println("===modify===");
        Person p = (Person) c.newInstance();
        System.out.println(p);
        p.say();

        System.out.println("===origin===");
        Person person = new Person();
        person.say();
    }

    @Test
    public void testLinkageError() throws Exception {
        Person orig = new Person();
        orig.say();
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(PERSON_CLASS);
        // 修改的类永远不能被JVM先加载，否则toClass()将失败
        Class aClass = cc.toClass();
    }

    @Test
    public void testClassCastException() throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("CCE");

        // 应用服务器上的ClassCastException
        CodeSource codeSource = new CodeSource(null, new CodeSigner[]{});
        ProtectionDomain protectionDomain = new ProtectionDomain(codeSource, null);
        cc.toClass(getClass().getClassLoader(), protectionDomain); // 使用对应的类加载器获取
    }

    @Test
    public void testLoader() throws Throwable {
        ClassPool pool = ClassPool.getDefault();
        Loader loader = new Loader(pool); // extend ClassLoader

        // 添加加载事件监听器
        loader.addTranslator(pool, new CustomTranslator()); // 类加载监听器

        // 加载类
        loader.loadClass("xxx"); // 不会调用 toBytecode() 或 writeFile()

        // 直接运行main
        String[] args = {};
        loader.run("MainClass", args);


        // loader对于
        // ClassPool中的CtClass不会委派给父类加载器
        // 调用 Loader::delegateLoadingOf() 方法的类会委派给父类加载器
        // 该搜索顺序允许Javassist加载修改后的类
        // 一旦由类加载器加载了一个类，该类中引用的其他类也将由父类加载器加载，因此它们永远不会被修改

    }

    @Test
    public void testNotice() {
        // To use the Loader, first put the class file under the ./class directory,
        // which must not be included in the class search path.
        // Otherwise, the class would be loaded by the default system class loader.
        // The directory name ./class is specified by insertClassPath() in the ClassPool.
    }

    @Test
    public void testChangeSystemClass() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("java.lang.String");
        CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
        f.setModifiers(Modifier.PUBLIC);
        cc.addField(f);
        cc.writeFile(".");

        // System.out.println(String.class.getField("hiddenValue").getName());

        // java -Xbootclasspath/p:. MyApp arg1 arg2...

        // 注意：不应使用此技术来覆盖系统类的应用程序，因为这样做会违反Java 2 Runtime Environment二进制代码许可证
    }

    @Test
    public void testRuntimeReLoad() {
        // 如果在启用JPDA（Java平台调试器体系结构）的情况下启动JVM，则可以动态重载类。
        // JVM加载一个类之后，可以卸载该类定义的旧版本，并可以重新加载一个新版本。也就是说，可以在运行时动态修改该类的定义。
        // 但是，新的类定义必须在一定程度上与旧的类兼容。JVM不允许在两个版本之间进行模式更改。它们具有相同的方法和字段集。
        // @see javassist.util.HotSwapper
    }

    static class CustomTranslator implements Translator {
        // addTranslator 时就被调用
        @Override
        public void start(ClassPool pool) throws NotFoundException, CannotCompileException {
        }

        // 加载类之前被调用，可以修改类的定义
        @Override
        public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
        }
    }

    static class Person {
        public void say() {
            System.out.println(1);
        }
    }
}
