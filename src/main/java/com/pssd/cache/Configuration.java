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

import java.lang.reflect.Field;

/**
 * Programmatic cache configuration. In our scheme, caches are
 * owned by a bean class. This uses a Fluent API
 * so you may chain calls as you would with a builder.
 * <p/>
 * @author _SHartsock
 * <p/>
 * see: http://ehcache.org/documentation/recipes/programmatic
 */
public class Configuration {
    public static final int DEFAULT_MAX_ELEMENTS_IN_MEMORY = 1000;
    public static final String DEFAULT_STORE_EVICTION_POLICY = "LRU";
    public static final String DEFAULT_CONFIGURATION_NAME = "configuration";
    String owner;
    Integer maxElementsInMemory;
    MemoryStoreEvictionPolicy memoryStoreEvictionPolicy;

    public Configuration() {
        this(LambdaCache.class);
    }

    public Configuration(Class owner) {
        this(owner, DEFAULT_MAX_ELEMENTS_IN_MEMORY);
    }

    public Configuration(Class owner, Integer maxElementsInMemory) {
        this(owner, maxElementsInMemory, DEFAULT_STORE_EVICTION_POLICY);
    }

    public Configuration(final Class owner, final String memoryStoreEvictionPolicy) {
        this(owner, DEFAULT_MAX_ELEMENTS_IN_MEMORY, memoryStoreEvictionPolicy);
    }

    public Configuration(final Class owner, final Integer maxElementsInMemory, final String memoryStoreEvictionPolicy) {
        this.setOwner(owner);
        this.maxElementsInMemory = maxElementsInMemory;
        this.setMemoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
    }

    public Configuration(final Integer maxElementsInMemory, final String memoryStoreEvictionPolicy) {
        this.maxElementsInMemory = maxElementsInMemory;
        this.setMemoryStoreEvictionPolicy(memoryStoreEvictionPolicy);
    }

    public Configuration(Configuration configuration, String fieldName) {
        this.owner = configuration.owner;
        this.maxElementsInMemory = configuration.maxElementsInMemory;
        this.memoryStoreEvictionPolicy = configuration.memoryStoreEvictionPolicy;
        if(!DEFAULT_CONFIGURATION_NAME.equals(fieldName)) {
            this.owner = this.owner.concat("." + fieldName);
        }
    }

    /**
     * set cache to non-default name
     * @param owner - cache's owning class
     * @param name - field name of the cache such as 'secondaryCache'
     */
    public Configuration setOwner(Class owner, String name) {
        if(DEFAULT_CONFIGURATION_NAME.equals(name)) {
            this.setOwner(owner.getCanonicalName());
        } else {
            this.setOwner(owner.getCanonicalName() + "." + name);
        }
        return this;
    }

    /**
     * Set the class that owns this cache. Each cache will belong to one owning class.
     * @param owner
     */
    public Configuration setOwner(final Class owner) {
        this.setOwner(owner.getCanonicalName());
        return this;
    }

    public Configuration setOwner(final String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * What is the maximum size of this cache?
     * @param maxElementsInMemory
     */
    public Configuration setMaxElementsInMemory(final Integer maxElementsInMemory) {
        this.maxElementsInMemory = maxElementsInMemory;
        return this;
    }

    /**
     * Controls the cache eviction policy. This is how the system determines what to delete. And when.
     * <p/>
     * One of LRU,LFU, and FIFO
     * <ol>
     *     <li>LRU: Least Recently Used</li>
     *     <li>LFU: Least Frequently Used</li>
     *     <li>FIFO: First In, First Out</li>
     * </ol>
     * @param memoryStoreEvictionPolicy
     */
    public Configuration setMemoryStoreEvictionPolicy(final String memoryStoreEvictionPolicy) {
        this.memoryStoreEvictionPolicy = MemoryStoreEvictionPolicy.fromString(memoryStoreEvictionPolicy);
        return this;
    }

    public Configuration setMemoryStoreEvictionPolicy(final MemoryStoreEvictionPolicy memoryStoreEvictionPolicy) {
        this.memoryStoreEvictionPolicy = memoryStoreEvictionPolicy;
        return this;
    }

    /**
     * Cast this data holder object into a true EhCache configuration object.
     * @return a native EhCache configuration object
     */
    CacheConfiguration as() {
        final CacheConfiguration conf = new CacheConfiguration(owner,maxElementsInMemory);
        conf.setMemoryStoreEvictionPolicyFromObject(memoryStoreEvictionPolicy);
        return conf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuration)) return false;

        Configuration that = (Configuration) o;

        if (maxElementsInMemory != null ? !maxElementsInMemory.equals(that.maxElementsInMemory) : that.maxElementsInMemory != null)
            return false;
        if (memoryStoreEvictionPolicy != null ? !memoryStoreEvictionPolicy.equals(that.memoryStoreEvictionPolicy) : that.memoryStoreEvictionPolicy != null)
            return false;
        if (!owner.equals(that.owner)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + (maxElementsInMemory != null ? maxElementsInMemory.hashCode() : 0);
        result = 31 * result + (memoryStoreEvictionPolicy != null ? memoryStoreEvictionPolicy.hashCode() : 0);
        return result;
    }
}

