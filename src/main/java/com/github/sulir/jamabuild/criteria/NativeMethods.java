package com.github.sulir.jamabuild.criteria;

import com.github.sulir.jamabuild.Project;
import com.github.sulir.jamabuild.filtering.AllowedPhases;
import com.github.sulir.jamabuild.filtering.Criterion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.List;
import java.util.stream.Stream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@AllowedPhases(Criterion.Phase.POST_BUILD)
public class NativeMethods extends Criterion {

    public NativeMethods(Phase phase, Type type) {
        super(phase, type);
    }

    @Override
    public boolean isMet(Project project) {
        Path jarsDir = project.getJARsDir();
        Path dependenciesDir = project.getDependenciesDir();

        if (Files.exists(jarsDir)) {
            if (Files.exists(dependenciesDir)) {
                try (Stream<Path> jarsPaths = Files.walk(jarsDir)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".jar"));
                     Stream<Path> dependenciesPaths = Files.walk(dependenciesDir)
                             .filter(Files::isRegularFile)
                             .filter(p -> p.toString().toLowerCase().endsWith(".jar"))) {
                    List<String> allJarFiles = Stream.concat(jarsPaths, dependenciesPaths)
                            .map(Path::toString)
                            .toList();

                    return hasNativeMethodCallInJARs(allJarFiles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try (Stream<Path> jarsPaths = Files.walk(jarsDir)
                        .filter(Files::isRegularFile)
                        .filter(p -> p.toString().toLowerCase().endsWith(".jar"))) {
                    List<String> allJarFiles = jarsPaths
                            .map(Path::toString)
                            .toList();

                    return hasNativeMethodCallInJARs(allJarFiles);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean hasNativeMethodCallInJARs(List<String> allJarFiles) throws IOException {
        for (String jarFile : allJarFiles) {
            if (hasNativeMethodCallInJAR(jarFile)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasNativeMethodCallInJAR(String jarFilePath) throws IOException {
        AtomicBoolean hasNativeMethod = new AtomicBoolean(false);

        try (JarFile jarFile = new JarFile(jarFilePath)) {
            Enumeration<JarEntry> entries = jarFile.entries();

            while (entries.hasMoreElements() && !hasNativeMethod.get()) {
                JarEntry entry = entries.nextElement();
                if (entry.isDirectory() || !entry.getName().endsWith(".class")) {
                    continue;
                }
                byte[] buffer = jarFile.getInputStream(entry).readAllBytes();
                ClassReader classReader = new ClassReader(buffer);
                classReader.accept(new ClassVisitor(Opcodes.ASM9) {
                    @Override
                    public MethodVisitor visitMethod(int access,
                                                     String name,
                                                     String descriptor,
                                                     String signature,
                                                     String[] exceptions) {
                        if ((access & Opcodes.ACC_NATIVE) != 0) {
                            hasNativeMethod.set(true);
                        }
                        return super.visitMethod(access, name, descriptor, signature, exceptions);
                    }
                }, 0);
            }
        }

        return hasNativeMethod.get();
    }
}