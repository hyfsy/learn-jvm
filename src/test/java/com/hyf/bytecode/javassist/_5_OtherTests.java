package com.hyf.bytecode.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.*;
import org.junit.Test;

import java.io.*;

/**
 * 对于简单的类文件，使用 ClassFileWriter 性能更好
 *
 * @author baB_hyf
 * @date 2021/05/03
 */
public class _5_OtherTests {
    private static final String INPUT_FILE  = "/com/hyf/javassist/MakeClass.class";
    private static final String OUTPUT_FILE = "E://MakeClass.class";

    @Test
    public void testGetClassFile() throws Exception {

        // 文件中获取
        InputStream resource = getClass().getResourceAsStream(INPUT_FILE);
        DataInputStream dis = new DataInputStream(resource);
        ClassFile cf = new ClassFile(dis);
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE)));
        cf.write(dos);
        dos.close();
        dis.close();
    }

    @Test
    public void testCreateClassFile() throws Exception {
        ClassFile cf = new ClassFile(false, "com.hyf.javassist.MakeClass", "java.lang.Object");
        cf.setInterfaces(new String[]{"java.lang.Cloneable"});

        // 添加
        FieldInfo fieldInfo = new FieldInfo(cf.getConstPool(), "create_field", "Ljava.lang.String;");
        fieldInfo.setAccessFlags(AccessFlag.PUBLIC | AccessFlag.STATIC);
        cf.addField(fieldInfo);

        // 删除
        // List fields = cf.getFields();
        // fields.remove(0);

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE)));
        cf.write(dos);
        dos.close();
    }

    @Test
    public void testVisitMethodCode() throws Exception {
        InputStream resource = getClass().getResourceAsStream(INPUT_FILE);
        DataInputStream dis = new DataInputStream(resource);
        ClassFile cf = new ClassFile(dis);
        MethodInfo methodInfo = cf.getMethod("a");
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute(); // Code
        CodeIterator it = codeAttribute.iterator();

        while (it.hasNext()) {
            int index = it.next(); // 下一条指令的索引
            // it.move(index); // 移动到指定索引处
            // it.begin(); // 移动到第一条指令

            int op = it.byteAt(index);// 返回索引处的无符号8位值
            // it.u16bitAt(index); // 返回索引处的无符号16位值
            // it.write(new byte[] {0}, index); // 在索引处写入一个字节数组
            // it.insert(index, new byte[] {0}); // 在索引处插入一个字节数组。分支偏移量等将自动调整

            System.out.println(Mnemonic.OPCODE[op]);

        }

        dis.close();
    }

    @Test
    public void testWriteByteCode() throws Exception {
        InputStream resource = getClass().getResourceAsStream(INPUT_FILE);
        DataInputStream dis = new DataInputStream(resource);
        ClassFile cf = new ClassFile(dis);
        ConstPool cp = cf.getConstPool();

        // 方法
        MethodInfo methodInfo = new MethodInfo(cp, "createdMethod", "(Ljava/lang/String;Ljava/lang/Integer;)I");
        methodInfo.setAccessFlags(AccessFlag.PUBLIC);

        // 方法指令
        Bytecode bytecode = new Bytecode(cp, 2, 3);
        bytecode.addAload(1);
        bytecode.addInvokestatic("java/lang/Integer", "parseInt", "(Ljava/lang/String;)I");
        bytecode.addAload(2);
        bytecode.addInvokevirtual(ClassPool.getDefault().get("java.lang.Integer"), "intValue", CtClass.intType, null);
        bytecode.addOpcode(Opcode.IADD);
        bytecode.addReturn(CtClass.intType);
        CodeAttribute codeAttribute = bytecode.toCodeAttribute();

        // 设置指令
        methodInfo.setCodeAttribute(codeAttribute);
        cf.addMethod(methodInfo);

        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(OUTPUT_FILE)));
        cf.write(dos);
        dos.close();
        dis.close();
    }

    @Test
    public void testAnnotationOp() throws Exception {
        InputStream resource = getClass().getResourceAsStream(INPUT_FILE);
        DataInputStream dis = new DataInputStream(resource);
        ClassFile cf = new ClassFile(dis);

        AttributeInfo attribute = cf.getAttribute(AnnotationsAttribute.visibleTag);

        dis.close();
    }

    @Test
    public void testGenericOp() {
        // C<T> extends ArrayList<T> implements Serializable
        CtClass.intType.setGenericSignature("<T:Ljava/lang/Object;>Ljava/util/ArrayList<TT;>;Ljava/io/Serializable;");
    }

    @Test
    public void testVarargs() {
        // methodName(String... strs) -> Modifier.VARARGS
    }

    @Test
    public void testPreVerify() throws Exception {

        // 修改方法后自动重置 StackMap
        MethodInfo.doPreverify = true;

        ClassPool pool = ClassPool.getDefault();
        CtClass stringClass = pool.get("String");
        CtMethod[] declaredMethods = stringClass.getDeclaredMethods();
        for (CtMethod declaredMethod : declaredMethods) {
            MethodInfo methodInfo = declaredMethod.getMethodInfo();
            methodInfo.rebuildStackMap(pool); // 手动重置
        }
    }

    @Test
    public void testBox() {
        // 不支持自动装箱、拆箱
        // X: Integer i = 1;
        // √: Integer i = new Integer(1);
    }

    @Test
    public void testDebug() {
        CtClass.debugDump = "./dump"; // 导出Class文件的base路径
    }
}
