package com.hyf.bytecode.lombok;

import lombok.Getter;
import lombok.Setter;

/**
 * Lombok插件只能显示自己注解类里的相关方法或变量
 * <p>
 * 不支持显示编译后的字节码内的方法或变量
 *
 * @author baB_hyf
 * @date 2021/05/01
 */
public class Hello {

    public static void main(String[] args) {
        Person person = new Person();
    }

    @Getter
    @Setter
    static class Person {
        private Integer id;
        private String  name;
    }
}
