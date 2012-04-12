package com.pssd.cache;

public class SimpleCacheUser {
    static Configuration configuration = new Configuration(SimpleCacheUser.class,100,"LRU");
    static Configuration fifoCache = new Configuration(SimpleCacheUser.class,10,"FIFO");

    public String input(Integer number) {
        return (String) CacheFactory.cache(SimpleCacheUser.class, number, new CacheableFunction<Integer, String>() {
            public String cached(Integer number) {
                // Your magic goes here.
                return number.toString() + "!";
            }
        });
    }

    public Integer named(String number) {
        return (Integer) CacheFactory.cache(fifoCache, number, new CacheableFunction<String,Integer>() {
            public Integer cached(String number) {
                // Your magic goes here.
                return Integer.parseInt(number.replaceAll("\\D",""));
            }
        });
    }
}
