package work.share.reflection.api;


public interface ObjectReflector extends Reflector {


    Object getOriginalObject();


    Object getValue(String name);

    void setValue(String name, Object value);


//    /**
//     * 集合类的元素添加操作
//     * @param element
//     */
//    void add(Object element);


    Object call(String methodName, Object... args);

}
