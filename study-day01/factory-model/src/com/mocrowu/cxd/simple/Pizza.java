package com.mocrowu.cxd.simple;

public abstract class Pizza {
    /**
     * 准备披萨
     */
    public abstract void prepare();

    /**
     *  烤披萨
     */
    public abstract void bake();

    /**
     *  切披萨
     */
    public abstract void cut();

    /**
     *  把披萨装入盒中
     */
    public abstract void box();
}
