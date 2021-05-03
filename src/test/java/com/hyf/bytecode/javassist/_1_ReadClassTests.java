package com.hyf.bytecode.javassist;

import javassist.*;
import org.junit.Test;

import java.io.InputStream;

/**
 * 文档：http://www.javassist.org/tutorial/tutorial.html
 *
 * @author baB_hyf
 * @date 2021/05/01
 */
public class _1_ReadClassTests {

    @Test
    public void testCreateSomething() throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        CtClass makeClass = pool.makeClass("MakeClass");
        CtClass iMakeClass = pool.makeInterface("IMakeClass");
        CtMethod i_method = CtNewMethod.abstractMethod(CtClass.voidType, "i_method", null, null, iMakeClass);
        iMakeClass.addMethod(i_method); // 需要add

        // CtNewMethod.make()
        // makeClass.addMethod();

    }

    @Test
    public void testReadClass() throws Exception {
        // 默认搜索路径与JVM相同
        ClassPool pool = ClassPool.getDefault();
        // 类路径
        pool.insertClassPath(new ClassClassPath(this.getClass()));
        // 本地
        pool.insertClassPath("E://test");
        // 网络
        // com.hyf.package._1_ReadClassTests -> http://www.baidu.com:8080/get/com/hyf/package/Hello.class
        ClassPath urlClassPath = new URLClassPath("www.baidu.com", 8080, "/get/", "com.hyf.package.");
        pool.insertClassPath(urlClassPath);

        // byte[]
        ClassPath byteArrayClassPath = new ByteArrayClassPath("FullQualifierClassName", new byte[]{});
        pool.insertClassPath(byteArrayClassPath);
        // CtClass className = pool.get("FullQualifierClassName");

        // InputStream
        InputStream is = null;
        // CtClass ctClass = pool.makeClass(is); // don't know the full class name

        // 对于jar文件，使用makeClass()可能会提高性能。
        // 由于ClassPool对象按需读取类文件，因此它可能会在整个jar文件中重复搜索每个类文件。
        // makeClass()可用于优化此搜索。将CtClass通过构建makeClass()被保存在ClassPool对象和类文件是永远不会再次读取。
        // 可以扩展类搜索路径。定义一个新的类实现ClassPath接口
    }
}
