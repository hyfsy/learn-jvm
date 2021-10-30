package com.hyf.bytecode.asm;

import com.hyf.bytecode.asm.pojo.B;
import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * 文档：https://asm.ow2.io/asm4-guide.pdf
 * <p>
 * idea插件：asm bytecode outline.
 * 点击 java文件，右击 show bytecode outline就可以查看到asm自动生成的代码了
 *
 * @author baB_hyf
 * @date 2021/04/30
 */
public class Main {

    @Test
    public void generateConstructor() throws Exception {
        AsmUtils.getInstrumentation().addTransformer(new AsmUtils.Transform("com.hyf\\.(.*?)\\.B"), false);
        AsmUtils.getInstrumentation().retransformClasses(B.class);

        Constructor<B> constructor = B.class.getConstructor(Boolean.class);
        B b = constructor.newInstance(true);
        System.out.println(b);
    }
}
