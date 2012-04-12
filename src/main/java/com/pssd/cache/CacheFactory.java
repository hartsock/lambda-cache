package com.pssd.cache;

import com.pssd.cache.errors.ConfigurationFoundButNotAccessableException;
import com.pssd.cache.errors.NoConfigurationFoundException;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

/**
 * The basic idea here is to build caches based on objects.
 * <p/>
 * @author Shawn Hartsock
 */
public class CacheFactory {
    /**
     * The singleton owner of caches in this system.
     */
    final static CacheManager cacheManager = new CacheManager();

    /**
     * builds a CacheConfiguration object based on properties
     * @param owner - class to name the cache after
     * @param maxElementsInMemory - how big to make the cache
     * @param memoryStoreEvictionPolicy - when to evict
     * @return  - an object suitable for building caches from
     */
    static CacheConfiguration configure(final Class owner, final Integer maxElementsInMemory, final String memoryStoreEvictionPolicy)  {
        return new Configuration(owner,maxElementsInMemory,memoryStoreEvictionPolicy).as();
    }

    /**
     * returns the default configuration for a class
     * @param owner - class to examine
     * @return  - default configuration
     */
    static Configuration configureDefault(final Class owner) {
        return configure(owner, Configuration.DEFAULT_CONFIGURATION_NAME);
    }

    /**
     * examines a single field on a class for a valid configuration
     * @param owner - class to examine
     * @param name - name of configuration
     * @return - the derived configuration based on the field name
     */
    static Configuration configure(final Class owner, final String name) {
        Configuration configuration = new Configuration(owner);
        try {
            Field field = owner.getDeclaredField(name);
            configuration = getConfiguration(field);
        } catch (NoSuchFieldException e) {
            throw new NoConfigurationFoundException(owner,name);
        }
        return configuration;
    }

    /**
     * gets a single instance of a configuration based on a field from reflection API not for general use.
     * @param field - field to examine
     * @return - the object found normalized to the field name.
     */
    private static Configuration getConfiguration(final Field field) {
        Configuration configuration = null;
        field.setAccessible(true);
        try {
            configuration = new Configuration((Configuration) field.get(null), field.getName());
        } catch (IllegalAccessException e) {
            throw new ConfigurationFoundButNotAccessableException(field.getDeclaringClass(),field.getName());
        } finally {
            return configuration;
        }
    }

    /**
     * Returns the array of all cache configurations on a class.
     * @param owner - cache to examine
     * @return  - array of all cache configurations found.
     */
    public static Configuration[] configure(final Class owner) {
        Collection<Configuration> configurations = new LinkedList<Configuration>();
        for(Field field: owner.getDeclaredFields()) {
            if(field.getType().equals(Configuration.class)) {
                Configuration configuration = getConfiguration(field);
                configurations.add(configuration);
            }
        }
        return configurations.toArray(new Configuration[0]);
    }

    /**
     * An internal helper function for getting a cache by a Configuration POJO. If the cache does not
     * exist, this method will create it using the configuration object.
     * @param configuration
     * @return
     */
    public static Ehcache getCache(final Configuration configuration) {
        Ehcache cache = null;
        String name = configuration.owner;
        cache = getCache(name);
        cache = (cache == null) ? createCache(configuration) : cache;

        return cache;
    }

    /**
     * gets a cache by name or returns null when no cache by that name is found.
     * @param name
     * @return
     */
    public static Ehcache getCache(String name) {
        Ehcache cache;
        try {
            cache = cacheManager.getCache(name);
        } catch(Throwable t) {
            synchronized (cacheManager) {
                cacheManager.removeCache(name);
            }
            cache = null;
            t.printStackTrace();
        }
        return cache;
    }

    /**
     * Creates a cache based on a configuration, registers it, then returns the created cache. Not for general use.
     * @param configuration
     * @return
     */
    private static Ehcache createCache(Configuration configuration) {
        Ehcache cache = new Cache(configuration.as());
        synchronized (cacheManager) {
            cacheManager.addCache(cache);
        }
        return cache;
    }

    /**
     * The caching method calls the other method of the same name with the configuration on the owning object found
     * by using the reflection API. If no configuration is found it throws the
     * @param owner
     * @param parameter
     * @param cacheableFunction
     * @return
     */
    public static Object cache(Class owner, Object parameter, CacheableFunction cacheableFunction) {
        Configuration config = configureDefault(owner);
        if(config == null) throw new NoConfigurationFoundException(owner);
        return cache(config,parameter,cacheableFunction);
    }

    /**
     * Caches are driven by a cache configuration. Wherever the configuration comes from, this method will
     * find the associated cache and then use your parameter object as a key to the cache.
     * <p/>
     * @param configuration - unique identifier for the cache the Configuration object is a POJO used as an index
     * @param parameter  - unique POJO you build to map the formal parameters to a method onto actual parameters
     * @param cacheableFunction - the code to call should the the parameter miss the cache specified by configuration
     * @return - the cached result
     */
    public static Object cache(final Configuration configuration, final Object parameter, final CacheableFunction cacheableFunction) {
        final Ehcache cache = getCache(configuration);
        Object result = null;
        try {
            result = cache.get(parameter);
        } catch (Throwable t) {
            result = null;
        }
        result = (result == null) ? put(cache,parameter,cacheableFunction) : result;
        cache.put(new Element(parameter,result));
        return result;
    }

    /**
     * puts an object onto the cache based on the parameter object as the key. Uses the cacheable function to place
     * the object. Not for general consumption as this will not intelligently handle cache collisions.
     * @param cache
     * @param parameter
     * @param cacheableFunction
     * @return
     */
    private static Object put(final Ehcache cache, final Object parameter, final CacheableFunction cacheableFunction) {
        final Object result = cacheableFunction.cached(parameter);
        cache.put(new Element(parameter,result));
        return result;
    }
}
