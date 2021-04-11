package com.hyf.jvm.oom;

import java.util.ArrayList;
import java.util.List;

/**
 * VM Args: -Xms10m -Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 *
 * @author baB_hyf
 * @date 2021/04/05
 */
public class HeapOOM {
    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true) {
            list.add(new OOMObject());
        }
    }

    static class OOMObject {
    }
}
