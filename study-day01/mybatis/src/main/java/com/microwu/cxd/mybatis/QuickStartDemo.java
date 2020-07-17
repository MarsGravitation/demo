package com.microwu.cxd.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description:
 *
 * SqlSessionFactoryBuilder：这个类可以被实例化、使用和丢弃，一旦创建了 SqlSessionFactory 就不需要他了。因此 SqlSessionFactoryBuilder 实例
 *                          的最佳作用域是方法作用域。你可以重用 SqlSessionFactoryBuilder 来创建多个 SqlSessionFactory 实例，但最好还是不要一直保留它，
 *                          以保证所有的XML 解析资源可以被释放给更重要的事情
 * SqlSessionFactory：一旦被创建就应该在应用的运行期间一直存在，没有任何理由丢弃它或者重新创建一个实例。使用 SqlSessionFactory 的最佳实践是在应用运行期间不要重复创建多次。
 *                      因此 sqlSessionFactory 的最佳作用域应该是应用作用域。最简单的就是使用单例模式或者静态单例模式
 * SqlSession：每个线程都应该有自己的 SqlSession 实例。它不是线程安全的，因此是不能被共享的。
 *
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/16   16:25
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class QuickStartDemo {
    public static void main(String[] args) throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
        // 每个基于 MyBatis 的应用都是以一个 SqlSessionFactory 的实例为核心的。
        // SqlSessionFactory 的实例可以通过 SqlSessionFactoryBuilder 获得
        // 而 SqlSessionFactoryBuilder 则可以从 XML 配置文件中或预先配置的 Configuration 实例来构建出 SqlSessionFactory 实例
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        try (SqlSession session = sqlSessionFactory.openSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            User user = mapper.selectUser(1L);
            System.out.println(user);
        }
    }
}