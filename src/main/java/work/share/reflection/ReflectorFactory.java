/**
 * Copyright 2009-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package work.share.reflection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReflectorFactory {
    private boolean classCacheEnabled = true;
    private final ConcurrentMap<Class<?>, ReflectorDelegate> reflectorMap = new ConcurrentHashMap<>();

    private static ReflectorFactory instance = new ReflectorFactory();

    private ReflectorFactory() {
    }


    public static boolean isClassCacheEnabled() {
        return instance.classCacheEnabled;
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled) {
        instance.classCacheEnabled = classCacheEnabled;
    }


    public static ReflectorDelegate findForClass(Class<?> type) {
        if (instance.classCacheEnabled)
            return instance.reflectorMap.computeIfAbsent(type, ReflectorDelegate::new);
        else
            return new ReflectorDelegate(type);
    }

    public static ReflectorFactory getInstance() {
        return instance;
    }


}
