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
     * 事务传播行为：指的是当一个事务方法被另一个事务方法调用时，这个事务方法应该如何进行
     *
     *      @Transactional(xxx)
     *      public void methodA() {
     *      methodB();
     *          // do something
     *      }
     *
     *
     *      @Transactional(xxx)
     *      public void methodB() {
     *          // do something
     *      }
     *
     *  a. PROPAGATION_REQUIRED
     *      如果存在一个事务，则支持当前事务。如果没有事务则开启一个新的事务。
     *
     *      单独调用 methodB，因为当前上下文不存在事务，所以会开启一个新的事务。
     *      调用 methodA 方法时，因为当前上下文不存在事务，所以会开启一个新的事务。当执行到
     *      methodB 时，methodB 发现当前上下文有事务，因此就加入到当前事务中来。
     *
     *  b. PROGAGATION_SUPPORTS
     *      如果存在一个事务，支持当前事务。如果没有事务，则非事务的执行。但是对于事务同步的
     *      事务管理器，PROPAGATION_SUPPORTS 与不适用事务有少许不同。
     *
     *      单独的调用 methodB 时，methodB 方法是非事务的执行的。当调用 methodA 时，methodB
     *      则加入了 methodA 的事务中，事务的执行。
     *
     *  c. PROPAGATION_MANDATORY
     *      如果已经存在一个事务，支持当前事务。如果没有一个活动事务。则抛出异常。
     *
     *      单独调用 methodB，因为当前没有一个活动事务，则会抛出 throw new IllegalTrasactionStateException
     *      当调用 methodA 时，methodB 则加入到 methodA 的事务中
     *
     *  d. PROPAGATION_REQUIRES_NEW
     *      使用 PROPAGATION_REQUIRES_NEW，需要使用 JtaTransactionMananger 作为事务管理器。
     *      它会开启一个新的事务。如果一个事务已经存在，则先将这个存在的事务挂起。
     *
     *     main(){
     *     TransactionManager tm = null;
     *     try{
     *      //获得一个JTA事务管理器
     *      tm = getTransactionManager();
     *      tm.begin();//开启一个新的事务
     *      Transaction ts1 = tm.getTransaction();
     *      doSomeThing();
     *      tm.suspend();//挂起当前事务
     *      try{
     *          tm.begin();//重新开启第二个事务
     *          Transaction ts2 = tm.getTransaction();
     *          methodB();
     *          ts2.commit();//提交第二个事务
     *      } Catch(RunTimeException ex) {
     *          ts2.rollback();//回滚第二个事务
     *      } finally {
     *          //释放资源
     *      }
     *     //methodB执行完后，恢复第一个事务
     *      tm.resume(ts1);
     *      doSomeThingB();
     *      ts1.commit();//提交第一个事务
     *      } catch(RunTimeException ex) {
     *          ts1.rollback();//回滚第一个事务
     *      } finally {
     *      //释放资源
     *      }
     *     }
     *
     *      在这里，我把 ts1 称为外层事务，ts2 称为内层事务。从上面的代码可以看出，ts2 和 ts1
     *      是两个独立的事务，互不相干。ts2 是否成功并不依赖于 ts1。如果 methodA 方法调用 methodB
     *      方法失败，而 methodB 方法所做的结果依然被提交了。而出了 mthodB 之外的其他代码导致
     *      的结果却被回滚了
     *
     *  e. PROPAGATION_NOT_SUPPORTED
     *      总是非事务地执行，并挂起任何存在的事务。同时也需要使用JtaTrancationMananger 作为事务管理器。
     *
     *      单独执行 methodB，无事务
     *      执行方法 A，执行到 methodB 时，会挂起事务 A，然后以非事务的方式执行 methodB
     *
     *  f. PROPAGATION_NEVER
     *      总是非事务地执行，如果存在一个活动事务，则抛出异常
     *
     *  g. PROPAGATION_NESTED
     *      如果一个活动的事务存在，则运行在一个嵌套的事务中。如果没有活动事务，则按
     *      PROOAGATION_REQUIRED 属性执行。
     *
     *      这是一个嵌套事务，使用 JDBC 3.0 驱动时，仅仅支持 DataSourceTransactionManager 作为事务管理器
     *
     *      需要 JDBC 驱动的 java.sql.Savepoint 类。使用 PROPAGATION_NESTED，还需要把 PlatformTransactionManager
     *      的 nestedTrasactionAllowed 属性设为 true
     *
     *      main(){
     *          Connection con = null;
     *          Savepoint savepoint = null;
     *          try{
     *              con = getConnection();
     *              con.setAutoCommit(false);
     *              doSomeThingA();
     *              savepoint = con2.setSavepoint();
     *              try{
     *                  methodB();
     *              } catch(RuntimeException ex) {
     *                  con.rollback(savepoint);
     *              } finally {
     *                  //释放资源
     *              }
     *              doSomeThingB();
     *              con.commit();
     *          } catch(RuntimeException ex) {
     *              con.rollback();
     *          } finally {
     *              //释放资源
     *          }
     *
     *      当 methodB 方法调用之前，调用 setSavepoint 方法，保存当前的状态到 savepoint。
     *      如果 methodB 方法调用失败，则恢复到之前保存的状态。但是需要注意的是，这时的事务并
     *      没有进行提交，如果后续的代码 doSmoeThingB 方法调用失败，则回滚包括 methodB 方法
     *      的所有操作。嵌套事务一个非常重要的概念就是内层事务依赖于外层事务。外层事务失败时，会
     *      回滚内层事务所做的动作。而内层事务操作失败并不会引发外层事务的回滚
     *
     *
     *  https://blog.csdn.net/soonfly/article/details/70305683
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2020/12/21  14:36
     *
     * @param
     * @return  org.springframework.jdbc.datasource.DataSourceTransactionManager
     */
    @Bean
    @SuppressWarnings("all")
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