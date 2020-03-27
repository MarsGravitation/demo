package com.microwu.cxd.repository;

import com.microwu.cxd.domain.Account;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/11/11   16:01
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public interface HelloRepository extends ElasticsearchCrudRepository<Account, Long> {
}