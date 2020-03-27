package com.microwu.cxd.spring;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/1/15   15:24
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringActivityApplicationTest {

    @Autowired
    private RepositoryService repositoryService;

    /**部署流程定义*/
    @Test
    public void deploy(){
        Deployment deployment = repositoryService.createDeployment()//创建一个部署对象
                .name("helloworld入门程序")
                .addClasspathResource("diagram/leave.bpmn")//从classpath的资源中加载，一次只能加载一个文件
                .addClasspathResource("diagram/leave.png")
                .deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署名称："+deployment.getName());
    }

}