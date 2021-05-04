package com.hyf.jvm.cls.loading;

/**
 * 有包名的类无法引用无包名的类
 * 无包名、且在同一个文件夹下的类之间可以互相引用
 * <p>
 * 无包名的类实际上被类加载器加载成功，但是放到了default包名下，所以无法访问
 * default包下同名类不会产生冲突，但会随机访问一个类
 * <p>
 * 有包名的类可通过反射访问无包名的类
 *
 * @author baB_hyf
 * @date 2021/05/04
 */
public class NonPackageClass {
}
