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
package org.share.reflection.wrapper;


import org.share.reflection.facade.ObjectFacade;
import org.share.reflection.property.PropertyTokenizer;

import java.util.Map;

/**
 * @author Clinton Begin
 */
public class MapWrapper extends BaseWrapper {

  private final Map<String, Object> map;

  public MapWrapper(ObjectFacade metaObject, Map<String, Object> map) {
    super(metaObject);
    this.map = map;
  }


  @Override
  public Object get(PropertyTokenizer prop) {
    if (prop.getIndex() != null) {
      Object collection = resolveCollection(prop, map);
      return getCollectionValue(prop, collection);
    } else {
      return map.get(prop.getName());
    }
  }

  @Override
  public void set(PropertyTokenizer prop, Object value) {
    if (prop.getIndex() != null) {
      Object collection = resolveCollection(prop, map);
      setCollectionValue(prop, collection, value);
    } else {
      map.put(prop.getName(), value);
    }
  }







//
//  @Override
//  public ObjectFacade instantiatePropertyValue(String name, PropertyTokenizer prop) {
//    HashMap<String, Object> map = new HashMap<>();
//    set(prop, map);
//    return ObjectFacade.forObject(map);
//  }





}
