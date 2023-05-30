package com.github.sulir.jamabuild.filtering;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedPhases {
    Criterion.Phase[] value();
}
