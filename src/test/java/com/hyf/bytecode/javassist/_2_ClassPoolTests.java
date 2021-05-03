package com.hyf.bytecode.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import org.junit.Test;

/**
 * @author baB_hyf
 * @date 2021/05/03
 */
public class _2_ClassPoolTests {

    @Test
    public void testClassPool() throws Exception {
        // ClassPool中存储所有的CtClass对象及所有的CtClass实例对象（编译需要）
        // CtClass太多容易导致内存泄漏

        ClassPool pool = ClassPool.getDefault();
        CtClass detachClass = pool.makeClass("DetachClass");
        // 从常量池中移除
        detachClass.detach();

        // 创建新的，丢弃旧的，需要手动回收旧的
        ClassPool classPool = new ClassPool(true);
        // new ClassPool() == ClassPool.getDefault()

        // 级联类池
        ClassPool parent = new ClassPool();
        ClassPool child = new ClassPool(parent);
        // child.appendClassPath()
        child.childFirstLookup = true; // 默认行为和类加载器类似，此处指定优先从子类池中查找

        CtClass one = pool.get("One"); // Hashtable
        one.setName("Two");
        CtClass two = pool.get("Two"); // one == two
        System.out.println("one == two: " + (one == two));
    }

    @Test
    public void testRenameClass() throws Exception {
        ClassPool pool= ClassPool.getDefault();
        CtClass cc = pool.get("oldClass");
        cc.writeFile("target/classes"); // freeze
        // cc.setName("newClass"); // error
        CtClass cc2 = pool.getAndRename("oldClass", "newClass");
    }

}
