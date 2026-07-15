package com.microwu.spi;

/**
 * Description:
 *
 * SPI Service Provider Interface
 *  - 用于实现模块化和可插拔性的机制
 *  - 内置的服务发现机制，可以用来启用框架扩展和替换组件
 *
 * <a href="https://www.cnblogs.com/mumubear/articles/18588126">...</a>
 * <a href="https://javaguide.cn/java/basis/spi.html#%E6%80%BB%E7%BB%93">...</a>
 *
 * @author: chengxudong             chengxudong@eversec.com
 * Date:       2025/3/25   11:37
 * Update History:
 * Author        Time            Content
 */
public class SpiDemo {

    public static void main(String[] args) {
        LogUtil.log("hello world");
    }

}
