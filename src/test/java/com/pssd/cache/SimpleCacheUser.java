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

