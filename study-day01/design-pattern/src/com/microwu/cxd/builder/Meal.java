package com.microwu.cxd.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Author:         Administration             chengxudong@microwu.com
 * Date:           2019/4/3   16:54
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class Meal {
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item){
        items.add(item);
    }

    public float getCost(){
        float cost = 0.0f;
        for(Item item : items){
            cost += item.price();
        }
        return cost;
    }

    public void showItems(){
        for(Item item : items){
            System.out.print("Item:" + item.name());
            System.out.print(",Packing:" + item.packing().pack());
            System.out.println(",Price:" + item.price());
        }
    }

}