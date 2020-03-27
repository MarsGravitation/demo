package com.microwu.cxd.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * @Descrip public abstract class Enum<E extends Enum<E>>
 *         implements Comparable<E>, Serializable
 * @author 成旭东
 */
public enum SingletonEnum {
    INSTANCE("单例模式"){
        public void show(){
            System.out.println("name" + this.name());
        }
    };
    private String name;

    SingletonEnum(String name){
        this.name = name;
    }

    public abstract void show();

    public static Map<String, SingletonEnum> map = new HashMap<>();
    static{
        for(SingletonEnum singletonEnum : SingletonEnum.values()){
            map.put(singletonEnum.name, singletonEnum);
        }
    }

}
