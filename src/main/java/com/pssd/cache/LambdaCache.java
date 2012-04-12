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

