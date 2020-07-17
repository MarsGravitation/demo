package com.microwu.cxd.jpa.repository;

import com.microwu.cxd.jpa.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/6/16   14:03
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    @Query("select u from User u where u.mobile = :mobile")
    User findByCustomeMobile(String mobile);

    @Query("select u from User u")
    List<User> list();
}
