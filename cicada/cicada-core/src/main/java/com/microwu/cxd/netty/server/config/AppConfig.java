package com.microwu.cxd.netty.server.config;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/8/10   14:46
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class AppConfig {

    /**
     * simple singleton Object
     */
    private static AppConfig config;

    private String rootPackageName;
    private String rootPath;
    private Integer port = 7317;

    private AppConfig() {

    }

    public static AppConfig getInstance() {
        if (config == null) {
            config = new AppConfig();
        }
        return config;
    }

    public String getRootPackageName() {
        return rootPackageName;
    }

    public void setRootPackageName(String rootPackageName) {
        this.rootPackageName = rootPackageName;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

}