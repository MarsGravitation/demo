package com.mocrowu.cxd.adapter;

public class BioRobot implements Robot {
    @Override
    public void cry() {
        System.out.println("仿生机器人叫。。。。。。。。。。。");
    }

    @Override
    public void run() {
        System.out.println("仿生机器人跑。。。。。。。。。。。");
    }
}
