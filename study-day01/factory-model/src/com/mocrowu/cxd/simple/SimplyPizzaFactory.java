package com.mocrowu.cxd.simple;

/**
 *  简单工厂模式分析：
 *      Factory：工厂角色，通过不同的参数创建不同的对象
 *      Product：抽象产品角色
 *      ConcreteProduct：具体的产品角色
 */
public class SimplyPizzaFactory {
    private Pizza pizza;
    public Pizza createPizza(String type){
        if(type == null || type == ""){
            return null;
        }else if(type == "c"){
            return new ChinesePizza();
        } else if(type == "e"){
            return new EnglishPizza();
        } else{
            return new ChinesePizza();
        }

    }
}
