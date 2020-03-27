package com.microwu.cxd.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/17   10:55
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Configuration
@MapperScan(basePackages = MysqlConfig.PACKAGE, sqlSessionFactoryRef = "sqlSessionFactory")
public class MysqlConfig {
    public static final String PACKAGE = "com.microwu.cxd.mapper";
    public static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";

    @Value("${spring.datasource-mysql.url}")
    private String dbUrl;
    @Value("${spring.datasource-mysql.username}")
    private String username;
    @Value("${spring.datasource-mysql.password}")
    private String password;
    @Value("${spring.datasource-mysql.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource-mysql.druid.initial-size}")
    private int initialSize;
    @Value("${spring.datasource-mysql.druid.min-idle}")
    private int minIdle;
    @Value("${spring.datasource-mysql.druid.max-active}")
    private int maxActive;
    @Value("${spring.datasource-mysql.druid.max-wait}")
    private int maxWait;
    @Value("${spring.datasource-mysql.druid.time-between-eviction-runs-millis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource-mysql.druid.min-evictable-idle-time-millis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource-mysql.druid.validation-query}")
    private String validationQuery;
    @Value("${spring.datasource-mysql.druid.test-while-idle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource-mysql.druid.test-on-borrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource-mysql.druid.test-on-return}")
    private boolean testOnReturn;
    @Value("${spring.datasource-mysql.druid.pool-prepared-statements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource-mysql.druid.max-pool-prepared-statement-per-connection-size}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource-mysql.druid.filters}")
    private String filters;
    @Value("{spring.datasource-mysql.druid.connection-properties}")
    private String connectionProperties;

    /**
     * 创建基于Druid连接池的数据源
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/17  11:19
     *
     * @param   	
     * @return  javax.sql.DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        DruidDataSource mysqlDataSource = new DruidDataSource();
        mysqlDataSource.setUrl(dbUrl);
        mysqlDataSource.setUsername(username);
        mysqlDataSource.setPassword(password);
        mysqlDataSource.setDriverClassName(driverClassName);
        mysqlDataSource.setInitialSize(initialSize);
        mysqlDataSource.setMinIdle(minIdle);
        mysqlDataSource.setMaxActive(maxActive);
        mysqlDataSource.setMaxWait(maxWait);
        mysqlDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        mysqlDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        mysqlDataSource.setValidationQuery(validationQuery);
        mysqlDataSource.setTestWhileIdle(testWhileIdle);
        mysqlDataSource.setTestOnBorrow(testOnBorrow);
        mysqlDataSource.setTestOnReturn(testOnReturn);
        mysqlDataSource.setPoolPreparedStatements(poolPreparedStatements);
        mysqlDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);

        mysqlDataSource.setFilters(filters);

        mysqlDataSource.setConnectionProperties(connectionProperties);

        return mysqlDataSource;
    }

    /**
     * 创建事务管理器
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/17  11:21
     *
     * @param
     * @return  org.springframework.jdbc.datasource.DataSourceTransactionManager
     */
    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(dataSource());
    }
    
    /**
     * 创建SQLSession工厂
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/17  11:22
     *
     * @param   	
     * @return  org.apache.ibatis.session.SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();

    }

}