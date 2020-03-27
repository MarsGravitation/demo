package com.mocrowu.cxd.abstracts;

import java.util.ArrayList;
import java.util.List;

public abstract class Pizza {
    String name; // 名字
    String dough; // 面团
    String sause; // 酱料
    protected List<String> topping = new ArrayList<String>(); // 佐料

    public void prepare(){
        System.out.println("prepare" + name);
        System.out.println("Tossing dough");
        System.out.println("Adding sause");
        System.out.println("Adding topping");
        for (int i = 0; i < topping.size(); i++){
            System.out.println("    " + topping.get(i));
        }
    }

    public void bake(){
        System.out.println("烤制");
    }

    public void cut(){
        System.out.println("切");
    }

    public void box(){
        System.out.println("打包");
    }

    public String getName() {
        return name;
    }
}
