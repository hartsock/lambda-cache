package com.pssd.cache;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CacheFactoryConfigurationMethodTests {

    @Test
    public void testSimpleStaticConfigure() {
        CacheConfiguration conf = CacheFactory.configure(CacheFactory.class, 100, "LRU");
        assertNotNull(conf);
        assertEquals(CacheFactory.class.getCanonicalName(),conf.getName());
        assertEquals(100,conf.getMaxEntriesLocalHeap());
        assertEquals(MemoryStoreEvictionPolicy.LRU,conf.getMemoryStoreEvictionPolicy());
    }


    @Test
    public void testSimpleConfigureByClass() {
        Configuration configuration = CacheFactory.configureDefault(SimpleCacheUser.class);
        assertNotNull(configuration);
        assertCorrectDefaultCacheConfiguration(configuration);
    }

    @Test
    public void testAlternateSimpleCacheConfigureByClass() {
        Configuration configuration = CacheFactory.configure(SimpleCacheUser.class,"fifoCache");
        assertNotNull(configuration);
        assertCorrectFifoCacheConfiguration(configuration);
    }

    @Test
    public void testAllSimpleConfigureByClass() {
        Configuration[] configs = CacheFactory.configure(SimpleCacheUser.class);
        assertNotNull(configs);
        assertTrue(configs.length == 2);
        assertCorrectDefaultCacheConfiguration(configs[0]);
        assertCorrectFifoCacheConfiguration(configs[1]);
    }

    private void assertCorrectDefaultCacheConfiguration(Configuration configuration) {
        assertEquals(SimpleCacheUser.class.getCanonicalName(),configuration.owner);
        assertEquals(100,configuration.maxElementsInMemory.intValue());
        assertEquals(MemoryStoreEvictionPolicy.LRU, configuration.memoryStoreEvictionPolicy);
    }

    private void assertCorrectFifoCacheConfiguration(Configuration configuration) {
        assertEquals(SimpleCacheUser.class.getCanonicalName() + ".fifoCache",configuration.owner);
        assertEquals(10,configuration.maxElementsInMemory.intValue());
        assertEquals(MemoryStoreEvictionPolicy.FIFO, configuration.memoryStoreEvictionPolicy);
    }

}
