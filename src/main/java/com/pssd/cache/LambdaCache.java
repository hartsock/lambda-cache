package com.pssd.cache;

/**
 * a generic Cache that takes a lambda
 * @param <P> : parameter to the cache
 * @param <T> : type returned by the cacheableFunction instance
 * <p/>
 * We use lambdas to create a generic cache for a single data type.
 */
public interface LambdaCache<P,T> {
    T invoke(CacheableFunction<P,T> cacheableFunction, P parameter);
}