package com.microwu.cxd.service.factory;

import com.microwu.cxd.domain.factory.Cart;
import com.microwu.cxd.domain.factory.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description: 模板方法模式
 *
 * 在父类中实现了购物车处理的流程模板，然后把需要特殊处理的地方留空白，
 * 就是留抽象方法定义，让子类去实现其中的逻辑。由于父类的逻辑不完整无法
 * 单独工作，因此需要定义为抽象类
 *
 *
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2023/2/1   14:36
 * Update History:
 * Author        Time            Content
 */
public abstract class AbstractCart {

    /**
     * 处理购物车的大量重复逻辑在父类实现
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/2/1 14:48
     *
     * @param: userId - [long]
     * @param: items - [java.util.Map<java.lang.Long]
     * @return: com.microwu.cxd.domain.factory.Cart
     */
    public Cart process(long userId, Map<Long, Integer> items) {
        Cart cart = new Cart();

        List<Item> itemList = new ArrayList<>();
        items.entrySet().stream().forEach(entry -> {
            Item item = new Item();
            itemList.add(item);
        });
        cart.setItems(itemList);
        // 让子类处理每一个商品的优惠
        itemList.stream().forEach(item -> {
            processCouponPrice(userId, item);
            processDeliveryPrice(userId, item);
        });
        //计算商品总价
        cart.setTotalItemPrice(cart.getItems().stream().map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add));
        //计算总运费
        cart.setTotalDeliveryPrice(cart.getItems().stream().map(Item::getDeliveryPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        //计算总折扣
        cart.setTotalDiscount(cart.getItems().stream().map(Item::getCouponPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        //计算应付价格
        cart.setPayPrice(cart.getTotalItemPrice().add(cart.getTotalDeliveryPrice()).subtract(cart.getTotalDiscount()));
        return cart;
    }

    /**
     * 处理商品优惠的逻辑留给子类实现
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/2/1 15:01
     *
     * @param: userId - [long]
     * @param: item - [com.microwu.cxd.domain.factory.Item]
     * @return: void
     */
    protected abstract void processDeliveryPrice(long userId, Item item);

    /**
     * 处理配送费的逻辑留给子类实现
     *
     * @author   chengxudong               chengxudong@eversec.com
     * @date    2023/2/1 15:01
     *
     * @param: userId - [long]
     * @param: item - [com.microwu.cxd.domain.factory.Item]
     * @return: void
     */
    protected abstract void processCouponPrice(long userId, Item item);

}
