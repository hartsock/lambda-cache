package com.pssd.cache;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CacheFactoryTests {

    @Test
    public void testSimpleCacheUserCorrectResult() {
        SimpleCacheUser cacheUser = new SimpleCacheUser();
        String output = cacheUser.input(5);
        assertEquals("5!",output);

    }

    @Test
    public void testSimpleCacheUserNamedCacheCorrectResult() {
        SimpleCacheUser cacheUser = new SimpleCacheUser();
        Integer output = cacheUser.named("5!");
        assertEquals(5,output.intValue());

    }
}
