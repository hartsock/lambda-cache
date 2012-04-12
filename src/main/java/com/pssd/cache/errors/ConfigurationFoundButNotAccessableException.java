package com.pssd.cache.errors;

public class ConfigurationFoundButNotAccessableException extends IllegalStateException {
    public ConfigurationFoundButNotAccessableException(Class owner, String name) {
        super("found static field " + name + " on " + owner.getCanonicalName() + " but could not access");
    }
}
