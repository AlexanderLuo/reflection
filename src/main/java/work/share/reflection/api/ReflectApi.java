package work.share.reflection.api;


import work.share.reflection.facade.ClassFacade;
import work.share.reflection.facade.ObjectFacade;

public class ReflectApi {


    public static ClassReflector forClass(Class<?> type) {
        return new ClassFacade(type);

    }

    public static ObjectReflector forObject(Object object){
        return  ObjectFacade.forObject(object);
    }

}
