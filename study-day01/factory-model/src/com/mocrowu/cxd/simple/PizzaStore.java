package com.mocrowu.cxd.simple;

/**
 * 简单工厂模式的有点：
 *  1.进行了责任分离，使用专门的类进行对象的创建
 *  2.如果对象类名比较复杂，可以通过简单的类型进行相关的映射
 *  3.可以通过配置文件，进一步提高系统的灵活性
 * 缺点：
 *      1.简单工厂模式集中了所有的产品创建，一旦工厂倒闭，则整个系统瘫痪
 *      2.会增加系统中类的个数，增加系统的复杂度和理解度
 *      3.系统扩展困难，一旦新增产品，就需要改动工厂类，如果产品较多时，工厂类难以维护
 *      4.简单工厂使用了静态工厂的方式，导致工厂角色无法基于继承的结构实现
 *  使用场景：
 *      一般用于产品类型较少，它最大的优点就是将创建对象和对象的使用分离
 */
public class PizzaStore {
    private SimplyPizzaFactory simplyPizzaFactory;

    public PizzaStore(SimplyPizzaFactory simplyPizzaFactory) {
        this.simplyPizzaFactory = simplyPizzaFactory;
    }

    public Pizza orderPizza(String type){
        Pizza pizza;
        pizza = simplyPizzaFactory.createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }

    public static void main(String[] args) {
        PizzaStore pizzaStore = new PizzaStore(new SimplyPizzaFactory());
        pizzaStore.orderPizza("c");

    }
}
