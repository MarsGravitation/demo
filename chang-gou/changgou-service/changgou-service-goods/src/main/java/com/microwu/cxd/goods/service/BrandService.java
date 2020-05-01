package com.microwu.cxd.goods.service;

import com.microwu.cxd.cg.goods.Brand;
import com.microwu.cxd.goods.mapper.BrandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/29   11:10
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public List<Brand> findAll() {
        return brandMapper.findAll();
    }

}