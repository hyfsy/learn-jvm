package com.hyf.bytecode.javassist;

/**
 * 文档：http://www.javassist.org/tutorial/tutorial.html
 * <p>
 * 缺点：
 * 1.不支持枚举、泛型、和添加注解
 * 2.不支持编译内部类或匿名类
 * 3.不支持continue和break语句
 * 4.编译器未正确实现方法分派
 * <p>
 * 推荐：
 * 建议用户将#用作类名和静态方法或字段名之间的分隔符，以便编译器可以快速解析表达式
 * <code>
 *     javassist.CtClass#intType.getName()
 * </code>
 *
 * @author baB_hyf
 * @date 2021/05/03
 */
public class Main {
}
