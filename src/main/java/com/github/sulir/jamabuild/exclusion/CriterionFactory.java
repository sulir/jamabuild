package com.github.sulir.jamabuild.exclusion;

import com.github.sulir.jamabuild.criteria.AndroidSource;
import com.github.sulir.jamabuild.criteria.BashScript;

import java.util.Arrays;
import java.util.List;

public class CriterionFactory {
    private static final List<Class<? extends Criterion>> criteria = List.of(
            AndroidSource.class,
            BashScript.class);
    private final CriterionType type;

    public CriterionFactory(CriterionType type) {
        this.type = type;
    }

    public Criterion createCriterion(String text) throws Exception {
        String[] parts = text.split(" ", 2);
        String name = parts[0];
        String parameter = parts.length > 1 ? parts[1] : null;

        Class<?> clazz = criteria.stream()
                .filter(c -> c.getSimpleName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Criterion " + name + " not found"));
        AllowedTypes annotation = clazz.getAnnotation(AllowedTypes.class);
        List<CriterionType> allowedTypes = Arrays.asList(annotation.value());

        if (allowedTypes.contains(type)) {
            if (parameter == null)
                return (Criterion) clazz.getDeclaredConstructor().newInstance();
            else
                return (Criterion) clazz.getDeclaredConstructor(String.class).newInstance(parameter);
        } else {
            throw new IllegalArgumentException("Criterion " + name + " is not allowed for " + type);
        }
    }
}
