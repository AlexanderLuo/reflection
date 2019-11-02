/**
 *    Copyright 2009-2017 the original author or authors.
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
package work.share.reflection.wrapper;

import work.share.reflection.ReflectionException;
import work.share.reflection.invoker.Invoker;
import work.share.reflection.facade.ClassFacade;
import work.share.reflection.facade.ObjectFacade;
import work.share.reflection.property.PropertyTokenizer;


/**
 * @author Clinton Begin
 */
public class BeanWrapper extends BaseWrapper {

  private final Object object;
  private final ClassFacade metaClass;

  public BeanWrapper(ObjectFacade metaObject, Object object) {
    super(metaObject);
    this.object = object;
    this.metaClass = metaObject;
  }


  @Override
  public Object get(PropertyTokenizer prop) {
    if (prop.getIndex() != null) {
      Object collection = resolveCollection(prop, object);
      return getCollectionValue(prop, collection);
    } else {
      return getBeanProperty(prop, object);
    }
  }

  @Override
  public void set(PropertyTokenizer prop, Object value) {
    if (prop.getIndex() != null) {
      Object collection = resolveCollection(prop, object);
      setCollectionValue(prop, collection, value);
    } else {
      setBeanProperty(prop, object, value);
    }
  }






//  @Override
//  public ObjectFacade instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
//    ObjectFacade metaValue;
//    Class<?> type = getSetterType(prop.getName());
//    try {
//      Object newObject = objectFactory.create(type);
//      metaValue = ObjectFacade.forObject(newObject, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory(), metaObject.getReflectorFactory());
//      set(prop, newObject);
//    } catch (Exception e) {
//      throw new ReflectionException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause:" + e.toString(), e);
//    }
//    return metaValue;
//  }



  /********************************************************************************************************************
   *  Real Work
  ********************************************************************************************************************/
  private Object getBeanProperty(PropertyTokenizer prop, Object object){
    try {
      Invoker method = metaClass.getGetInvoker(prop.getName());
      return method.invoke(object, NO_ARGUMENTS);
    }catch (Throwable t){
      throw new ReflectionException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
    }
  }

  private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
    try {
      Invoker method = metaClass.getSetInvoker(prop.getName());
      Object[] params = {value};
      method.invoke(object, params);

    } catch (Throwable t) {
      throw new ReflectionException("Could not set property '" + prop.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + t.toString(), t);
    }
  }



  @Override
  public void add(Object element) {
    throw new UnsupportedOperationException();
  }


}
