package com.microwu.cxd.jpa.repository;

import com.microwu.cxd.jpa.domain.UserAndAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Description:
 *
 * @Author: Administration             chengxudong@microwu.com
 * Date:           2020/6/17   15:18
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */

public interface AccountRepository extends JpaRepository<UserAndAccount, Long> {

    @Query(value = "select new com.microwu.cxd.jpa.domain.UserAndAccount(u.id, a.name, a.balance) from User u join Account a on u.id = a.id where a.id = :id")
    UserAndAccount find1(Long id);

    @Query(value = "select u.id, a.name username, a.balance from user u join account a on u.id = a.id where a.id = :id", nativeQuery = true)
    List<Object[]> find2(Long id);

    @Query(value = "select u.id, a.name username, a.balance from user u join account a on u.id = a.id where a.id = :id", nativeQuery = true)
    UserAndAccount find3(Long id);

    @Query(value = "select u.id, a.name username, a.balance from user u join account a on u.id = a.id where a.id in :list", nativeQuery = true)
    List<UserAndAccount> list(List<Long> list);

}
