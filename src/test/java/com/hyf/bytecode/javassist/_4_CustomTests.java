package com.hyf.bytecode.javassist;

import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.MethodInfo;
import javassist.expr.*;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author baB_hyf
 * @date 2021/05/03
 */
public class _4_CustomTests {

    public static final String CREATE_CLASS_NAME = "com.hyf.javassist.MakeClass";
    public static final String SAVE_PATH         = "target/classes";

    @Test
    public void testCustom() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass cc = pool.get("A");
        ClassFile classFile = cc.getClassFile();

        CtMethod ctMethod = CtNewMethod.make("", cc);
        MethodInfo methodInfo = ctMethod.getMethodInfo(); // method_info

        ClassMap classMap = new ClassMap();
        CtMethod copy = CtNewMethod.copy(ctMethod, cc, classMap);
    }

    @Test
    public void testChangeMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtMethod makeMethod = CtNewMethod.make("public void a() {System.out.println(\"invoked\");}", makeClass);
        makeMethod.insertBefore("{System.out.println(\"before invoke\"); System.out.println(\"before invoke finish\");}");
        makeMethod.insertAfter("if (1 == 1) {System.out.println(\"after invoke\");}", false);
        // makeMethod.insertAt(11, true, "src"); // 指定行号位置插入
        CtClass exType = ClassPool.getDefault().get("java.lang.RuntimeException");
        // 包裹"当前"方法体的所有内容，插入的代码片段必须以throw或return语句结尾
        makeMethod.addCatch("{ System.out.println($e); throw $e; }", exType);
        makeMethod.insertAfter("System.out.println(\"finally block\");", true);
        makeMethod.insertAfter("System.out.println(\"out of ex\");");
        makeClass.addMethod(makeMethod);
        makeClass.writeFile(SAVE_PATH);

    }

    @Test
    public void testCanUseParams() {
        // src不能访问在方法内声明的变量

        // $0, $1, $2, ...  this and actual parameters
        // $args	        An array of parameters. The type of $args is Object[].
        //                  The origin type will change to the wrapper type. Not include this.
        // $$	            All actual parameters.
        //                  For example, m($$) is equivalent to m($1,$2,...). Not include this.
        // $cflow(...)	    cflow variable
        //                  $cflow是当前线程的当前最顶层堆栈框架下的指定方法关联的堆栈框架数
        // $r	            The result type. It is used in a cast expression.
        // $w	            The wrapper type. It is used in a cast expression. e.g. Integer i = ($w)5;
        // $_	            The resulting value. casted result type.
        //                  used with $r, e.g. $_ = ($r)result;. cannot use setBody() method.
        // $sig	            An array of java.lang.Class objects representing the formal parameter types.
        // $type	        A java.lang.Class object representing the formal result type.
        // $class	        A java.lang.Class object representing the class currently edited.
        // $e               Exception instance in the catch block.
        // $proceed    	    origin call method name. not modified. @see ExprEditor
    }

    @Test
    public void testCFlow() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass("com.hyf.javassist.MakeCFlowClass");
        CtMethod makeMethod = CtNewMethod.make("public void recursion(int i) {System.out.println(\"invoked\");}", makeClass);
        makeMethod.useCflow("flowName");
        makeMethod.insertBefore("if ($cflow(flowName) == 0) System.out.println(\"flowName: \" + $1);");
        // makeMethod.insertAfter("recursion($1);");
        makeClass.addMethod(makeMethod);
        makeClass.writeFile(SAVE_PATH);
    }

    // 先调用 testCFlow() 方法
    @Test
    public void testMakeCFlowClassInvoke() throws Exception {
        Class<?> MakeCFlowClass = Class.forName("com.hyf.javassist.MakeCFlowClass");
        Method recursion = MakeCFlowClass.getDeclaredMethod("recursion", Integer.TYPE); // type not equal
        recursion.invoke(MakeCFlowClass.newInstance(), 1);
    }

    @Test
    public void testMakeClassInvoke() throws Exception {
        Class<?> MakeClass = Class.forName(CREATE_CLASS_NAME);
        Method recursion = MakeClass.getDeclaredMethod("a");
        recursion.invoke(MakeClass.newInstance());
    }

    @Test
    public void testChangeMethodBody() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtMethod makeMethod = CtNewMethod.make("public void a() {System.out.println(\"invoked\");}", makeClass);
        makeMethod.setBody(null); // null默认生成return;，非void返回类型，则生成0/null
        makeClass.addMethod(makeMethod);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testExprEditor() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtMethod makeMethod = CtNewMethod.make("public void a() {System.out.println(\"invoked\");}", makeClass);
        makeMethod.instrument(new ExprEditor() {
            // 更改表达式
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals("java.io.PrintStream") && m.getMethodName().equals("println")) {
                    // proceed代表了原本的println方法调用
                    m.replace("{ $1 = \"changed invoke\"; $_ = $proceed($$); }");
                }
            }

            public void edit(NewExpr e) { } // new创建对象的表达式
            public void edit(NewArray a) { } // new创建数组的表达式
            public void edit(ConstructorCall c) { } // 构造函数
            public void edit(FieldAccess f) { } // 字段
            public void edit(Instanceof i) { } // instanceof表达式
            public void edit(Cast c) { } // 显示类型转换的表达式
            public void edit(Handler h) { } // catch块的表达式
        });
        makeClass.addMethod(makeMethod);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testExprEditor_Handler() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtMethod makeMethod = CtNewMethod.make("public void a() {System.out.println(\"invoked\");}", makeClass);
        CtClass exType = ClassPool.getDefault().get("java.lang.RuntimeException");
        makeMethod.addCatch("{ System.out.println($e); throw $e; }", exType);
        makeMethod.instrument(new ExprEditor() {
            @Override
            public void edit(Handler h) throws CannotCompileException {
                h.insertBefore("System.out.println(\"invoke before\");");
            }
        });
        makeClass.addMethod(makeMethod);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testCreateMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtMethod makeMethod1 = CtNewMethod.make("public void a() {System.out.println(\"invoked\");}", makeClass);
        CtMethod makeMethod2 = CtNewMethod.make(Modifier.PUBLIC, CtClass.voidType, "b", null, null, "System.out.println(\"invoked\");", makeClass);
        CtMethod makeMethod3 = CtNewMethod.make("public int c(String s) { return $proceed(s); }", makeClass, "Integer", "parseInt");

        makeClass.addMethod(makeMethod1);
        makeClass.addMethod(makeMethod2);
        makeClass.addMethod(makeMethod3);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testCreateAbstractMethod() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        CtClass makeClass = pool.makeClass("com.hyf.javassist.MakeAbstractClass");
        // 也可稍后再添加方法体
        CtMethod makeMethod = new CtMethod(CtClass.voidType, "d", null, makeClass);

        // 变为抽象方法
        makeClass.addMethod(makeMethod);
        makeClass.setModifiers(makeMethod.getModifiers() | Modifier.ABSTRACT);

        // 添加方法体
        // makeMethod.setBody("int i = 0;");
        makeMethod.setBody("{Integer i = ($w)1; System.out.println(i.intValue());}");
        makeClass.setModifiers(makeMethod.getModifiers() & ~Modifier.ABSTRACT);

        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testRecursionInvoke() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);

        // 先构建抽象方法
        CtMethod m = CtNewMethod.make("public abstract int m(int i);", makeClass);
        CtMethod n = CtNewMethod.make("public abstract int n(int i);", makeClass);

        // 添加到类里就能引用了
        makeClass.addMethod(m);
        makeClass.addMethod(n);

        // 改变抽象方法为实际的方法
        m.setBody("{ return ($1 <= 0) ? 1 : (n($1 - 1) * $1); }");
        n.setBody("{ return m($1); }");
        makeClass.setModifiers(makeClass.getModifiers() & ~Modifier.ABSTRACT);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testCreateField() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtField string_field = CtField.make("String string_field = \"0\";", makeClass);
        CtField int_field = new CtField(CtClass.intType, "int_field", makeClass);

        makeClass.addField(string_field, "String.valueOf(222)"); // not end of semicolon
        makeClass.addField(int_field, "0 - 1"); // initial value

        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testRemoveSomething() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);

        CtField makeField = CtField.make("String field = \"0\";", makeClass);
        CtMethod makeMethod = CtNewMethod.make("public void a() { System.out.println(\"a\"); }", makeClass);
        CtConstructor makeConstructor = CtNewConstructor.make("public MakeClass(String s) { System.out.println(s); }", makeClass);

        // makeClass.addInterface();
        makeClass.addMethod(makeMethod);
        makeClass.addField(makeField);
        makeClass.addConstructor(makeConstructor);

        makeClass.removeField(makeField);
        makeClass.removeMethod(makeMethod);
        makeClass.removeConstructor(makeConstructor); // 会提供默认的
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testAnnotationDefaultValue() throws Exception {
        ClassPool pool = ClassPool.getDefault();

        // 不支持添加注解
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);

        makeClass.getAvailableAnnotations(); // 获取ClassPool中存在的注解
        Object[] annotations = makeClass.getAnnotations(); // 获取所有注解
        System.out.println(Arrays.toString(annotations));
    }

    @Test
    public void testImportPackage() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        pool.importPackage("java.awt"); // 使用其他三方包前，需要先导入，默认导入了java.lang
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME); // 还是得全类名
        CtField makeField = CtField.make("public Point p;", makeClass);

        makeClass.addField(makeField);
        makeClass.writeFile(SAVE_PATH);
    }

    @Test
    public void testRecommend() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass makeClass = pool.makeClass(CREATE_CLASS_NAME);
        CtField makeField = CtField.make("public String s;", makeClass);

        makeClass.addField(makeField, "javassist.CtClass#intType.getName()");

        makeClass.writeFile(SAVE_PATH);
    }
}
