/* License added by: GRADLE-LICENSE-PLUGIN
 *
 * Copyright (C) 2012 Shawn Hartsock <hartsock@acm.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

