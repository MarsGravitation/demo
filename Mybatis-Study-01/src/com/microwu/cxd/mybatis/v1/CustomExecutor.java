package com.microwu.cxd.mybatis.v1;

public interface CustomExecutor {
    public <T> T query(String statement, String parameter);
}
