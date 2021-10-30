package com.hyf.bytecode.bytebuddy;

import com.hyf.bytecode.bytebuddy.pojo.B;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.Test;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Constructor;

/**
 * @author baB_hyf
 * @date 2021/10/29
 */
public class Test_GetInstrument {

    @Test
    public void testGetInstrument() {
        Instrumentation instrumentation = ByteBuddyAgent.install();
        System.out.println(instrumentation);
    }

    @Test
    public void testCreateBooleanConstructor() throws Exception {
        ByteBuddyEnhancer.addTransform("com.hyf\\.(.*?)\\.B"); // 添加Transform后再使用类做转换
        // 类使用后，不能执行添加方法等操作
        ByteBuddyEnhancer.reTransform(B.class);

        Constructor<B> constructor = B.class.getConstructor(Boolean.class);
        B b = constructor.newInstance(true);
        System.out.println(b);
    }
}
