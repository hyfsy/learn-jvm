package com.hyf.bytecode.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import net.bytebuddy.agent.ByteBuddyAgent;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * @author baB_hyf
 * @date 2021/10/30
 */
public class AsmUtils {

    private static final Instrumentation instrumentation;

    static {
        instrumentation = install();
    }

    private static Instrumentation install() {
        return ByteBuddyAgent.install();
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public static void addTransformer(ClassFileTransformer transformer) {
        getInstrumentation().addTransformer(transformer, true /* true表示转换方法被调用后，是否还原之前转换好的，并重新进行转换，还是直接使用之前转换好的，不进行转换调用 */);
    }

    public static class Transform implements ClassFileTransformer {

        private final String regex;

        public Transform(String regex) {
            this.regex = regex;
        }

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

            // no-match
            if (!className.replace("/", ".").matches(regex)) {
                return classfileBuffer;
            }

            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(classfileBuffer);
            classReader.accept(classNode, Opcodes.ACC_PUBLIC);

            String aBooleanClassName = Boolean.class.getName().replace(".", "/");
            String initMethodDescription = "(L" + aBooleanClassName + ";)V";

            // exist
            for (MethodNode method : classNode.methods) {
                if (method.name.equals("<init>") && method.desc.equals(initMethodDescription)) {
                    return classfileBuffer;
                }
            }

            classNode.methods.removeIf(m -> m.name.equals("<init>") && m.desc.equals("()V"));
            MethodNode initMethodNode = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            InsnList initInitList = new InsnList();
            initInitList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            initInitList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, classNode.superName, "<init>", "()V", false));
            initInitList.add(new InsnNode(Opcodes.RETURN));
            initMethodNode.instructions = initInitList;
            classNode.methods.add(initMethodNode);

            MethodNode booleanInitMethodNode = new MethodNode(Opcodes.ACC_PUBLIC, "<init>", initMethodDescription, null, null);
            InsnList booleanInitList = new InsnList();
            booleanInitList.add(new VarInsnNode(Opcodes.ALOAD, 0));
            booleanInitList.add(new VarInsnNode(Opcodes.ALOAD, 1));
            booleanInitList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, aBooleanClassName, "booleanValue", "()Z", false));
            booleanInitList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, classNode.superName, "<init>", "(Z)V", false));
            booleanInitList.add(new InsnNode(Opcodes.RETURN));
            booleanInitMethodNode.instructions = booleanInitList;
            classNode.methods.add(booleanInitMethodNode);

            int flags = ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS;
            ClassWriter classWriter = new ClassWriter(classReader, flags);
            classNode.accept(classWriter);
            // saveToFile(classWriter);
            return classWriter.toByteArray();
        }

        private void saveToFile(ClassWriter classWriter) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(classWriter.toByteArray());
                 FileOutputStream fos = new FileOutputStream("E:\\B.class")) {
                int len;
                byte[] bytes = new byte[1024];
                while ((len = bais.read(bytes)) > 0) {
                    fos.write(bytes, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
