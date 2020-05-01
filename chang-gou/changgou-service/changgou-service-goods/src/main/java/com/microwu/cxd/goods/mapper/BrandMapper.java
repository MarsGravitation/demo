package com.microwu.cxd.goods.mapper;

import com.microwu.cxd.cg.goods.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/4/29   11:11
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Repository
public interface BrandMapper {

    List<Brand> findAll();
}