package com.microwu.cxd.jpa.service;

import com.microwu.cxd.jpa.domain.UserAndAccount;
import com.microwu.cxd.jpa.utils.EntityManagerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/17   16:34
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class AccountService {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public List find(Long id) {
        String sql = "select u.id, a.name username, a.balance from user u join account a on u.id = a.id where a.id = :id";
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        List relatives = EntityManagerUtil.findRelatives(sql, map, entityManager, UserAndAccount.class);
        return relatives;
    }
}