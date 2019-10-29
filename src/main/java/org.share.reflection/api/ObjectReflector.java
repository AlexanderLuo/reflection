package org.share.reflection.api;



public interface ObjectReflector extends Reflector {


    Object getOriginalObject();


    Object getValue(String name);
    void setValue(String name, Object value);





//    /**
//     * 集合类的元素添加操作
//     * @param element
//     */
//    void add(Object element);


    /**
     * 反射调用方法
     * @param methodName
     * @param args
     * @return
     */
    Object call(String methodName, Object... args);

}
