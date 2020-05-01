package com.microwu.cxd.goods;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@MapperScan("com.microwu.cxd.goods.mapper")
public class GoodsApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(GoodsApplication.class, args);
    }
}
