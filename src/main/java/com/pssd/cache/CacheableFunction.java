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

