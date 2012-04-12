package com.pssd.cache;

/**
 * This interface is intended for use as a lambda.
 * <p/>
 * CachableFunction <br/>
 * P: is the parameter <br/>
 * T: is the return type <br/>
 * <p/>
 * a simple generic interface that can be used in
 * a Functional Programming style that takes a Parameter
 * of type P and returns a value of type T.
 * <p/>
 * Example:
 * <pre>
 *  new CacheableFunction<Integer, String>() { public String cached(Integer number) {
 *          return number.toString() + "!";
 *  }};
 * </pre>
 */
public interface CacheableFunction<P,T> {
    T cached(P parameter);
}
