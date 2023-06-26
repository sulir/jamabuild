package com.github.sulir.jamabuild.filtering;

import com.github.sulir.jamabuild.criteria.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

public class CriterionFactory {
    private static final List<Class<? extends Criterion>> criteria = List.of(
            AndroidSource.class,
            BashScript.class,
            NativeMethods.class,
            SourceFile.class,
            UnresolvedReferences.class);
    private final Criterion.Phase phase;
    private final Criterion.Type type;

    public CriterionFactory(Criterion.Phase phase, Criterion.Type type) {
        this.phase = phase;
        this.type = type;
    }

    public Criterion createCriterion(String text) throws Exception {
        String[] parts = text.split(" ", 2);
        String name = parts[0];
        String parameter = parts.length > 1 ? parts[1] : null;

        Class<?> clazz = findClass(name);
        AllowedPhases annotation = clazz.getAnnotation(AllowedPhases.class);
        List<Criterion.Phase> allowedPhases = Arrays.asList(annotation.value());

        if (allowedPhases.contains(phase))
            return createInstance(clazz, parameter);
        else
            throw new IllegalArgumentException("Criterion " + name + " is not allowed for " + phase);
    }

    private Class<?> findClass(String name) {
        return criteria.stream()
                .filter(c -> c.getSimpleName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Criterion " + name + " not found"));
    }

    private Criterion createInstance(Class<?> clazz, String parameter) throws ReflectiveOperationException {
        if (parameter == null) {
            Constructor<?> constructor = clazz.getDeclaredConstructor(Criterion.Phase.class, Criterion.Type.class);
            return (Criterion) constructor.newInstance(phase, type);
        } else {
            Constructor<?> constructor = clazz.getDeclaredConstructor(Criterion.Phase.class, Criterion.Type.class,
                    String.class);
            return (Criterion) constructor.newInstance(phase, type, parameter);
        }
    }
}
