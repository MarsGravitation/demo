package com.microwu.collection;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/9/20   9:40
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class ListTest {
    public static void main(String[] args) {
//        ArrayList<String> strings = new ArrayList<>();
//        strings.add("a");
//
//        strings.remove(1);
//
//        ListIterator<String> stringListIterator = strings.listIterator();
//        stringListIterator.hasNext();
//
//        strings.subList(0 ,1);

        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("a");
        linkedList.iterator();

        HashMap<String, String> hashMap = new HashMap<>(16);
        hashMap.put("a", "b");

    }
}