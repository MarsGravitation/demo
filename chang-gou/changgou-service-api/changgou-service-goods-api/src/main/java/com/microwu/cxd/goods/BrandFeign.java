package com.microwu.cxd.goods;

import com.microwu.cxd.cg.goods.Brand;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Description: zuul 含有 hystrix 和 ribbon，直接代理服务就行，没必要在网关和服务之间
 *              加入feign，只会增加通讯消耗。这里这个feign 是为了服务间的通信使用的
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/4/29   13:49
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@FeignClient("goods")
public interface BrandFeign {

    @GetMapping("/brand/list")
    List<Brand> findAll();

}
