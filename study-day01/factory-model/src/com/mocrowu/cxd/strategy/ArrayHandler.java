package com.mocrowu.cxd.strategy;

public class ArrayHandler {
    private Sort sort;

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int[] sort(int[] arr){
        return sort.sort(arr);
    }
}
