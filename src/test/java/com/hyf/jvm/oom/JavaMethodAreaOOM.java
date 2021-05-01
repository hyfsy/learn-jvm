package com.hyf.jvm.oom;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * VM Args: -XX:PermSize=10M -XX:MaxPermSize=10M
 *
 * @author baB_hyf
 * @date 2021/04/05
 */
public class JavaMethodAreaOOM {
    public static void main(String[] args) {

        OOMObject oomObject = new OOMObject();
        OOMObjectHandler handler= new OOMObjectHandler(oomObject);

        while (true) {
            Proxy.newProxyInstance(OOMObject.class.getClassLoader(),
                    OOMObject.class.getInterfaces(), handler);
        }
    }

    interface I_OOM { }
    static class OOMObject implements I_OOM { }
    static class OOMObjectHandler implements InvocationHandler {
        private final OOMObject a_a;
        public OOMObjectHandler(OOMObject a_a) { this.a_a = a_a; }
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(a_a, args);
        }
    }
}
