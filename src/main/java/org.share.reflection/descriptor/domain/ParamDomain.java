package org.share.reflection.descriptor.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ParamDomain {

    private Method method;
    private int index;
    private Class<?> type;
    private String name;
    private Annotation[] annotations;





    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
