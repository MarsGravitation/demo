package com.microwu.cxd.goods.controller;

import com.microwu.cxd.cg.goods.Brand;
import com.microwu.cxd.entity.Result;
import com.microwu.cxd.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/29   11:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/list")
    public Result<List<Brand>> findAll() {
        List<Brand> brands = brandService.findAll();
        Result result = new Result();
        result.setData(brands);
        return result;
    }

}