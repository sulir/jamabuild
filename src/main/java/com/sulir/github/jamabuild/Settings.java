package com.sulir.github.jamabuild;

public record Settings(String timeout, Boolean skipTests) {
    public Settings(String timeout, Boolean skipTests) {
        this.timeout = timeout == null ? "1h" : timeout;
        this.skipTests = skipTests == null || skipTests;
    }
}
