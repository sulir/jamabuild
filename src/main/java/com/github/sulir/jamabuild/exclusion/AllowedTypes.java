package com.github.sulir.jamabuild.exclusion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedTypes {
    CriterionType[] value();
}
