package org.share.reflection.api;

import org.share.reflection.invoker.Invoker;

public interface   Reflector {
    boolean hasSetter(String name);
    boolean hasGetter(String name);
    boolean hasInvoker(String name);


    Invoker getGetInvoker(String name);
    Invoker getSetInvoker(String name);
    Invoker getInvoker(String methodName);


    Class<?> getSetterType(String name);
    Class<?> getGetterType(String name);


}
