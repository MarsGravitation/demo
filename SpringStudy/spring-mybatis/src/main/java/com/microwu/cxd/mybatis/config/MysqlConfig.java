package com.microwu.cxd.mybatis.config;

import com.microwu.cxd.mybatis.mapper.UserMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/16   15:44
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
public class MysqlConfig {

    /**
     * MyBatis-Spring 使用 SqlSessionFactoryBean 来创建 SqlSessionFactory
     * SqlSessionFactoryBean 实现了 FactoryBean 接口，意味着 bean 并不是 SqlSessionFactoryBean 本身，
     * 而是工厂类 SqlSessionFactoryBean.getObject 方法返回的结果
     *
     * 通常你不需要直接使用 SqlSessionFactoryBean，session 的工厂 bean 会被注入到 MapperFactoryBean 中
     *
     * DataSource 是必要的一个属性
     * configLocation 用来指定 MaBatis 的 XML 配置文件路径
     *
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/21  14:27
     *
     * @param
     * @return  org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());

        factoryBean.setMapperLocations();
        return factoryBean.getObject();
    }

    private DataSource dataSource() {
        return null;
    }

    /**
     * 为事务管理器指定的 DataSource 必须和 SqlSessionFactoryBean 是同一个数据源
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/21  14:36
     *
     * @param
     * @return  org.springframework.jdbc.datasource.DataSourceTransactionManager
     */
    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    
    /**
     * 用来替换 SqlSession，SqlSessionTemplate 是线程安全的
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/21  14:40
     *
     * @param   	
     * @return  org.mybatis.spring.SqlSessionTemplate
     */
    @Bean
    public SqlSessionTemplate sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    /**
     * 如果映射器接口 UserMapper 在相同的类路径下有对应的 XML 映射器配置文件，将会被 MapperFactoryBean 自动解析，
     * 不需要在 MyBatis 配置文件中显示配置映射器
     *
     * 发现映射器
     *  a. <mybatis:scan/>
     *      |- <mybatis:scan base-package="org.mybatis.spring.sample.mapper" />
     *      |- 自动注入 MapperFactoryBean，如果使用多数据源，自动注入可能存在问题，使用 factory-ref 指定
     *      |- 支持基于标记接口或者注解的过滤操作。可以指定特定注解或者父接口
     *  b. @MapperScan
     *      |- @Configuration
     *      |- @MapperScan("org.mybatis.spring.sample.mapper")
     *      |- public class AppConfig {
     *      |- // ...
     *      |- }
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/21  15:39
     *
     * @param
     * @return  org.mybatis.spring.mapper.MapperFactoryBean<com.microwu.cxd.mybatis.mapper.UserMapper>
     */
    @Bean
    public MapperFactoryBean<UserMapper> mapperMapperFactoryBean() throws Exception {
        MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<>(UserMapper.class);
        factoryBean.setSqlSessionFactory(sqlSessionFactory());
        return factoryBean;
    }

    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("");
        configurer.setSqlSessionFactoryBeanName("");
        return configurer;
    }

    @Bean
    public UserMapper userMapper() throws Exception {
        SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory());
        return sqlSessionTemplate.getMapper(UserMapper.class);
    }

}