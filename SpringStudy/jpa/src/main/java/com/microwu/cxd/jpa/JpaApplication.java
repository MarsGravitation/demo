package com.microwu.cxd.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 *  SpringData: 其实是Spring 提供了一个操作数据的框架，SpringData JPA 只是 SpringData 框架下的一个基于 JPA 标准操作数据的模块
 *  SpringData JPA：基于 JPA 的标准数据进行操作。简化操作持久层的代码。只需要编写接口就可以
 *
 *  核心接口
 *      1. Repository
 *      2. CrudRepository
 *      3. PagingAndSortingRepository
 *      4. JpaRepository
 *      5. JPASpecificationExecutor
 *
 *  测试：
 *      1. 方法名称命名查询方式
 *          extends Repository<Users,Integer>
 *          // 方法名称必须要遵循驼峰式命名规则，findBy（关键字）+属性名称（首字母大写）+查询条件（首字母大写）
 *          List<Users> findByName(String name);
 *
 *      2. 基于 @query 注解查询与更新
 *          extends JpaRepository<Users,Integer>
 *          @Query("from Users where name = ?")
 *          List<Users> queryByNameUseHQL(String name);
 *
 *          @Query(value = "select * from t_user where name=?",nativeQuery = true)
 *          List<Users> queryByNameUseSQL(String name);
 *
 *  其他：
 *      CrudRepository 继承 Repository，主要完成一些增删改查操作
 *      PagingAndSortingRepository 继承 CrudRepository 接口，提供了分页与排序操作
 *      JpaRepository 继承了 PagingAndSoringRepository 接口，对继承父接口中的返回值进行适配
 *
 *  补充：一对多、多对多需要以后进行测试
 *
 *
 * https://blog.csdn.net/qq_39086296/article/details/90485645
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/6/16   11:05
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class JpaApplication {
    public static void main(String[] args) {
        SpringApplication.run(JpaApplication.class, args);
    }
}