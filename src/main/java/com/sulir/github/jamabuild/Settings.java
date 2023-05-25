package com.sulir.github.jamabuild;

public record Settings(String timeout, Boolean skipTests, String[] preExclude, String[] postExclude) {
    public Settings(String timeout, Boolean skipTests, String[] preExclude, String[] postExclude) {
        this.timeout = timeout == null ? "1h" : timeout;
        this.skipTests = skipTests == null || skipTests;
        this.preExclude = preExclude == null ? new String[0] : preExclude;
        this.postExclude = postExclude == null ? new String[0] : postExclude;
    }
}
