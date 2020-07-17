package com.microwu.cxd.flowable;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/7/15   16:17
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class HolidayRequest {
    public static void main(String[] args) {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        // 实例化 ProcessEngine 实例
        // 这是一个线程安全的对象，通常在应用程序中只需要实例化一次
        // ProcessEngine 是从 ProcessEngineConfiguration 实例创建的
        // 允许你配置和调整 ProcessEngine 的设置。通常，ProcessEngineConfiguration
        // 是使用XML文件创建的，也可以编程创建它。Configuration 最小的配置是 JDBC 连接
        ProcessEngine processEngine = cfg.buildProcessEngine();

        // 部署到引擎，这意味着 流程引擎会将 XML 文件存储在数据库中；流程定义将转换成对象模型，这样就可以启动流程实例

        // RepositoryService 提供了管理与控制 deployments （部署）和 process definitions （流程定义）的操作
        // 流程定义是 Java 对象，体现流程中每一步的结构和行为。部署是引擎中的包装单元，一个部署可以包含多个 XML 文件
        // RepositoryService 可用于部署这样的包。部署意味着它将上传引擎，引擎会检查与分析所有的流程。在部署之后
        // 可以在系统中使用这个部署包，部署包中的所有流程都可以启动
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("holiday-request.bpmn20.xml")
                .deploy();

        // 流程定义，它定义了请假的各个步骤
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        // 启动流程实例
        Scanner scanner = new Scanner(System.in);

        System.out.println("Who ar you?");
        String employee = scanner.nextLine();

        System.out.println("How many holidays do you want to request?");
        Integer nrOfHolidays = Integer.valueOf(scanner.nextLine());

        System.out.println("Why do you need them?");
        String description = scanner.nextLine();

        // 启动流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        HashMap<String, Object> variables = new HashMap<>();
        variables.put("employee", employee);
        variables.put("nrOfHolidays", nrOfHolidays);
        variables.put("description", description);

        // 在流程实例启动后，会创建一个执行 execution，并将其放在启动事件上。从这里开始，这个执行沿着顺序流移动

        // RepositoryService 提供静态信息，RuntimeService 用于启动流程定义的新流程实例。
        // 同一时刻，一个流程定义通常有多个运行中的实例
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("holidayRequest", variables);

        // 所有任务相关的东西都组织在 TaskService
        TaskService taskService = processEngine.getTaskService();
        // 任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("managers").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ") " + tasks.get(i).getName());
        }

        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.valueOf(scanner.nextLine());
        Task task = tasks.get(taskIndex - 1);
        Map<String, Object> processVariables = taskService.getVariables(task.getId());
        System.out.println(processVariables.get("employee") + " wants " +
                processVariables.get("nrOfHolidays") + " of holidays. Do you approve this?");

        boolean approved = scanner.nextLine().toLowerCase().equals("y");
        variables = new HashMap<String, Object>();
        variables.put("approved", approved);
        taskService.complete(task.getId(), variables);

        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }

    }
}