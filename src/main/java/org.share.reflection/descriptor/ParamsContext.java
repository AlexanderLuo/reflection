/**
 * Copyright 2009-2018 the original author or authors.
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
package org.share.reflection.descriptor;

import org.share.reflection.descriptor.domain.ParamDomain;
import org.share.reflection.descriptor.utils.ParamNameUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiFunction;

public class ParamsContext {

    private int paramsSize;
    private final SortedMap<Integer, ParamDomain> paramDomainSortedMap;


    public ParamsContext(Method method, BiFunction<Class<?>, Annotation[], ParamDomain> mapper) {
        final SortedMap<Integer, ParamDomain> map = new TreeMap<>();
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Annotation[][] paramAnnotations = method.getParameterAnnotations();

        this.paramsSize = paramAnnotations.length;

        for (int paramIndex = 0; paramIndex < this.paramsSize; paramIndex++) {
            ParamDomain paramDomain;
            Class type = paramTypes[paramIndex];
            Annotation[] annotations = paramAnnotations[paramIndex];
            if (mapper != null) {
                paramDomain = mapper.apply(type, annotations);
            } else {
                paramDomain = new ParamDomain();
                paramDomain.setType(type);
                paramDomain.setName(getActualParamName(method, paramIndex));
            }
            map.put(paramIndex, paramDomain);
        }

        paramDomainSortedMap = Collections.unmodifiableSortedMap(map);
    }


    public int getParamsSize() {
        return paramsSize;
    }


    public SortedMap<Integer, ParamDomain> getParamDomainSortedMap() {
        return paramDomainSortedMap;
    }

    private String getActualParamName(Method method, int paramIndex) {
        return ParamNameUtil.getParamNames(method).get(paramIndex);
    }


}
