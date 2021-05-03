package com.hyf.bytecode;

/**
 * Javassist源代码级API比ASM中实际的字节码操作更容易使用，在复杂的字节码级操作上提供了更高级别的抽象层。
 * Javassist源代码级API只需要很少的字节码知识，甚至不需要任何实际字节码知识，因此实现起来更容易、更快。
 *
 * Javassist使用反射机制，这使得它比运行时使用Classworking技术的ASM慢。
 * 总的来说ASM比Javassist快得多，并且提供了更好的性能。Javassist使用Java源代码的简化版本，然后将其编译成字节码。
 * 这使得Javassist非常容易使用，但是它也将字节码的使用限制在Javassist源代码的限制之内。
 *
 * 总之，如果需要更简单的方法来动态操作或创建Java类，那么应该使用Javassist API 。如果需要注重性能地方，应该使用ASM库。
 *
 * @author baB_hyf
 * @date 2021/05/01
 */
public class Hello {

    public static void main(String[] args) {

    }
}
