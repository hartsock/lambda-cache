package com.pssd.cache.errors;

public class NoConfigurationFoundException extends IllegalStateException {
    public NoConfigurationFoundException(final Class owner) {
        super("could not find static field configuration on " + owner.getCanonicalName());
    }

    public NoConfigurationFoundException(final Class owner,final String name) {
        super("could not find static field " + name + " on " + owner.getCanonicalName());
    }
}
