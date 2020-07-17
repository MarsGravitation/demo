package com.microwu.cxd;

import com.microwu.cxd.jpa.JpaApplication;
import com.microwu.cxd.jpa.domain.User;
import com.microwu.cxd.jpa.domain.UserAndAccount;
import com.microwu.cxd.jpa.repository.AccountRepository;
import com.microwu.cxd.jpa.repository.UserRepository;
import com.microwu.cxd.jpa.service.AccountService;
import com.microwu.cxd.jpa.utils.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/16   14:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootTest(classes = {JpaApplication.class})
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Test
    public void test() {
        User user1 = userRepository.findByUsername("user_1");
        System.out.println(user1);

        User user2 = userRepository.findByCustomeMobile("18435202728");
        System.out.println(user2);

        List<User> list = userRepository.list();
        System.out.println(list);
    }

    @Test
    public void test02() {
        List<Object[]> list = accountRepository.find2(1L);
        List<UserAndAccount> userAndAccounts = EntityUtils.castEntity(list, UserAndAccount.class, new UserAndAccount());
        System.out.println(userAndAccounts);
    }

    @Test
    public void test03() {
        List list = accountService.find(10L);
        System.out.println(list);
    }

    @Test
    public void test04() {
        UserAndAccount accountRepository3 = accountRepository.find3(1L);
        System.out.println(accountRepository3);
    }

    @Test
    public void test05() {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(1L);
        longs.add(2L);
        List<UserAndAccount> list = accountRepository.list(longs);
        System.out.println(list);
    }

}