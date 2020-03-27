package com.microwu.cxd.mybatis.v2.session;

/**
 * Description: 创建SessionFactory工厂
 * Author:   Administration
 * Date:     2019/2/28 10:20
 * Copyright (C), 2015-2019, 北京小悟科技有限公司
 * History:
 * <author>          <time>          <version>          <desc>
 */
public class SqlSessionFactory {
    private CustomConfiguration configuration;

    /**
     * @Descrip build方法初始化configuration，具体工作在Configuration的构造器里完成
     * @author 成旭东
     * @date 2019/2/28 10:31
     * @param  * @param mapperPath
     * @return com.microwu.cxd.mybatis.v2.session.SqlSessionFactory
     */
    public SqlSessionFactory build(String mapperPath) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return this.build(mapperPath, null);
    }

    public SqlSessionFactory build(String mapperPath, String[] pluginPath) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        return this.build(mapperPath, pluginPath, false);
    }

    public SqlSessionFactory build(String mapperPath, String[] pluginPath, boolean enableCache) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        configuration = new CustomConfiguration(mapperPath, pluginPath, enableCache);
        return this;
    }

    /**
     * @Descrip 根据配置信息获取对应的SqlSession
     * @author 成旭东
     * @date 2019/2/28 10:25
     * @param  * @param configuration
     * @return com.microwu.cxd.mybatis.v2.session.SqlSessionFactory
     */
    public CustomSqlSession openSession(){
        return new CustomSqlSession(configuration);
    }
}