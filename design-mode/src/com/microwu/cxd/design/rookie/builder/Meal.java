package com.microwu.cxd.design.rookie.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/7/23   15:02
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Meal {
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public float getCost() {
        float cost = 0.0f;
        for(Item item : items) {
            cost += item.price();
        }
        return cost;
    }

    public void showItems() {
        for(Item item : items) {
            System.out.println("Item: " + item.name());
            System.out.println("Packing: " + item.packing().pack());
            System.out.println("Price: " + item.price());
        }
    }
}