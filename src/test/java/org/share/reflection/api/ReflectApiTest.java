package org.share.reflection.api;

import org.junit.Assert;
import org.junit.Test;
import org.share.reflection.UserEntity;
import work.share.reflection.api.ClassReflector;
import work.share.reflection.api.ObjectReflector;
import work.share.reflection.api.ReflectApi;

public class ReflectApiTest {


    static UserEntity userEntity = new UserEntity();
    static {
        userEntity.setAge(123);
        userEntity.setName("tester");
        userEntity.setAddr("hk");
    }

    @Test
    public void forObject(){

        ObjectReflector objectOperator = ReflectApi.forObject(userEntity);
        Assert.assertEquals(objectOperator.getValue("age"),123);
        Assert.assertEquals(objectOperator.getValue("name"),"tester");
        Assert.assertEquals(objectOperator.getValue("addr"),"hk");

    }



    @Test
    public void forClass(){
        ClassReflector operator = ReflectApi.forClass(UserEntity.class);
        Assert.assertTrue(operator.hasGetter("age"));
        Assert.assertTrue(operator.hasGetter("name"));
        Assert.assertTrue(operator.hasGetter("addr"));

    }
}
