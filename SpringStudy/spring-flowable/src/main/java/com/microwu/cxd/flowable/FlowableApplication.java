package com.microwu.cxd.flowable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Description:
 * 1. 内存中的数据源会自动创建（因为 H2 驱动程序再类路径中）并传递给 Flowable 流程引擎配置
 * 2. 创建并公开了 Flowable ProcessEngine
 * 3. 所有 Flowable 服务都作为 Spring bean 公开
 * 4. Spring Job Executor 已创建
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/16   9:52
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@SpringBootApplication
public class FlowableApplication {
    public static void main(String[] args) {
        SpringApplication.run(FlowableApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner init(final RepositoryService repositoryService,
//                                  final RuntimeService runtimeService,
//                                  final TaskService taskService) {
//
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... strings) throws Exception {
//                System.out.println("Number of process definitions : "
//                        + repositoryService.createProcessDefinitionQuery().count());
//                System.out.println("Number of tasks : " + taskService.createTaskQuery().count());
//                runtimeService.startProcessInstanceByKey("oneTaskProcess");
//                System.out.println("Number of tasks after process start: "
//                        + taskService.createTaskQuery().count());
//            }
//        };
//    }
}