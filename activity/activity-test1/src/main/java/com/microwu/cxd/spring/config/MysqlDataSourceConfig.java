package com.microwu.cxd.spring.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * Mysql数据源配置
 *
 * @author      Sage Ro             shengjie.luo@microwu.com
 * @date        2018/5/17  15:33
 * CopyRight    北京小悟科技有限公司    http://www.microwu.com
 *
 * Update History:
 *   Author        Time            Content
 */
@Configuration
@MapperScan(basePackages = MysqlDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "mysqlSqlSessionFactory")
@SuppressWarnings("Duplicates")
public class MysqlDataSourceConfig {

    static final String PACKAGE = "com.microwu.cxd.spring.mapper";
    private static final String MAPPER_LOCATION = "classpath:/mapper/*.xml";


    /**
     * DataSourceProperties
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2019-01-28  11:58
     *
     * @return DataSourceProperties
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource-mysql")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * 创建Druid数据库连接池Bean
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/17  16:42
     *
     * @return 数据源
     */
    @Bean("mysqlDataSource")
    @Primary
    @ConfigurationProperties("spring.datasource-mysql.druid")
    public DataSource mysqlDataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(DruidDataSource.class).build();
    }

    /**
     * 创建事务管理器
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/28  20:57
     *
     * @return 事务管理器
     */
    @Bean("mysqlTransactionManager")
    @Primary
    public DataSourceTransactionManager mysqlTransactionManager(DataSource dataSource) {
        List<Filter> proxyFilters = ((DruidDataSource) dataSource).getProxyFilters();
        // 关闭错误日志输出
        for (Filter proxyFilter : proxyFilters) {
            if (proxyFilter instanceof Slf4jLogFilter) {
                ((Slf4jLogFilter) proxyFilter).setStatementLogErrorEnabled(false);
            }
        }
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建sqlSession工厂
     *
     * @author      Sage Ro             shengjie.luo@microwu.com
     * @date        2018/5/28  20:57
     *
     * @param mysqlDataSource
     * @return sqlSession工厂
     * @throws Exception
     */
    @Bean("mysqlSqlSessionFactory")
    @Primary
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource mysqlDataSource) throws Exception {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(mysqlDataSource);
        sessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(MAPPER_LOCATION));
        return sessionFactoryBean.getObject();
    }
}
