/**
 *    Copyright 2009-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.share.reflection.facade;


import org.share.reflection.ReflectorDelegate;
import org.share.reflection.ReflectorFactory;
import org.share.reflection.api.ClassReflector;
import org.share.reflection.descriptor.TypeParameterResolver;
import org.share.reflection.invoker.GetFieldInvoker;
import org.share.reflection.invoker.Invoker;
import org.share.reflection.invoker.MethodInvoker;
import org.share.reflection.property.PropertyTokenizer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * @author Clinton Begin
 */
public class ClassFacade implements ClassReflector {

    static class NullClass { }
    private final ReflectorDelegate reflectorDelegate;

    public ClassFacade(Class<?> type) {
        this.reflectorDelegate = ReflectorFactory.findForClass(type);
    }


    public ClassFacade metaClassForProperty(String name) {
        Class<?> propType = reflectorDelegate.getGetterType(name);
        return new ClassFacade(propType);
    }
    private ClassFacade metaClassForProperty(PropertyTokenizer prop) {
        Class<?> propType = getGetterType(prop);
        return new ClassFacade(propType);
    }



    @Override
    public boolean hasSetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (reflectorDelegate.hasSetter(prop.getName())) {
                ClassFacade metaProp = metaClassForProperty(prop.getName());
                return metaProp.hasSetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return reflectorDelegate.hasSetter(prop.getName());
        }
    }

    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            if (reflectorDelegate.hasGetter(prop.getName())) {
                ClassFacade metaProp = metaClassForProperty(prop);
                return metaProp.hasGetter(prop.getChildren());
            } else {
                return false;
            }
        } else {
            return reflectorDelegate.hasGetter(prop.getName());
        }
    }


    @Override
    public boolean hasInvoker(String name) {
        return false;
    }


    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            ClassFacade metaProp = metaClassForProperty(prop.getName());
            return metaProp.getSetterType(prop.getChildren());
        } else {
            return reflectorDelegate.getSetterType(prop.getName());
        }
    }
    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            ClassFacade metaProp = metaClassForProperty(prop);
            return metaProp.getGetterType(prop.getChildren());
        }
        // issue #506. Resolve the type inside a Collection Object
        return getGetterType(prop);
    }



    /********************************************************************************************************************
     *  About Method
    ********************************************************************************************************************/
    @Override
    public Invoker getGetInvoker(String name) {
        return reflectorDelegate.getGetInvoker(name);
    }

    @Override
    public Invoker getSetInvoker(String name) {
        return reflectorDelegate.getSetInvoker(name);
    }

    @Override
    public Invoker getInvoker(String methodName) {
        return null;
    }


    /********************************************************************************************************************
     *  Real Work
    ********************************************************************************************************************/
    private Class<?> getGetterType(PropertyTokenizer prop) {
        Class<?> type = reflectorDelegate.getGetterType(prop.getName());
        if (prop.getIndex() != null && Collection.class.isAssignableFrom(type)) {
            Type returnType = getGenericGetterType(prop.getName());
            if (returnType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnType = actualTypeArguments[0];
                    if (returnType instanceof Class) {
                        type = (Class<?>) returnType;
                    } else if (returnType instanceof ParameterizedType) {
                        type = (Class<?>) ((ParameterizedType) returnType).getRawType();
                    }
                }
            }
        }
        return type;
    }

    private Type getGenericGetterType(String propertyName) {
        try {
            Invoker invoker = reflectorDelegate.getGetInvoker(propertyName);
            if (invoker instanceof MethodInvoker) {
                 Field _method = MethodInvoker.class.getDeclaredField("method");
                _method.setAccessible(true);
                 Method method = (Method) _method.get(invoker);
                return TypeParameterResolver.resolveReturnType(method, reflectorDelegate.getType());
            } else if (invoker instanceof GetFieldInvoker) {
                Field _field = GetFieldInvoker.class.getDeclaredField("field");
                _field.setAccessible(true);
                Field field = (Field) _field.get(invoker);
                return TypeParameterResolver.resolveFieldType(field, reflectorDelegate.getType());
            }
        } catch (NoSuchFieldException | IllegalAccessException ignored) {
        }
        return null;
    }




}
