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
package work.share.reflection.facade;

import work.share.reflection.api.ObjectReflector;
import work.share.reflection.property.PropertyTokenizer;
import work.share.reflection.wrapper.BeanWrapper;
import work.share.reflection.wrapper.MapWrapper;
import work.share.reflection.wrapper.ObjectWrapper;

import java.util.Map;

/**
 * @author Clinton Begin
 */
public class ObjectFacade extends ClassFacade implements ObjectReflector {
    public static ObjectFacade NULL = new ObjectFacade(NullClass.class);


    private final Object originalObject;
    private final ObjectWrapper objectWrapper;


    public ObjectFacade(Object object) {
        super(object.getClass());
        this.originalObject = object;


        if (object instanceof ObjectWrapper)
            this.objectWrapper = (ObjectWrapper) object;
        else if (object instanceof Map)
            this.objectWrapper = new MapWrapper(this, (Map) object);
//        else if (object instanceof Collection)
//            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        else
            this.objectWrapper = new BeanWrapper(this, object);

    }


    public ObjectFacade metaObjectForProperty(String name) {
        Object value = getValue(name);
        return ObjectFacade.forObject(value);
    }


    /********************************************************************************************************************
     *
     ********************************************************************************************************************/

    public Object getValue(String name) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            ObjectFacade metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == NULL) {
                return null;
            } else {
                return metaValue.getValue(prop.getChildren());
            }
        } else {
            return objectWrapper.get(prop);
        }
    }

    public void setValue(String name, Object value) {
        PropertyTokenizer prop = new PropertyTokenizer(name);
        if (prop.hasNext()) {
            ObjectFacade metaValue = metaObjectForProperty(prop.getIndexedName());
            if (metaValue == NULL) {
                if (value == null) {
                    // don't instantiate child path if value is null
                    return;
                } else {
//                    metaValue = objectWrapper.instantiatePropertyValue(name, prop, objectFactory);
                }
            }
            metaValue.setValue(prop.getChildren(), value);
        } else {
            objectWrapper.set(prop, value);
        }
    }


    @Override
    public Object getOriginalObject() {
        return originalObject;
    }


    @Override
    public Object call(String methodName, Object... args) {
        return null;
    }


    public static ObjectFacade forObject(Object object) {
        if (object == null)
            return NULL;
        return new ObjectFacade(object);
    }


}
