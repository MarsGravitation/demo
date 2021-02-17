package com.microwu.cxd.kafka.admin;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/8   14:08
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
public class OfficialAdmin {

    public static final Admin ADMIN;

    static {
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, "192.168.133.134:9092");
        ADMIN = KafkaAdminClient.create(properties);
    }

    public static void list() throws ExecutionException, InterruptedException {
        ListTopicsResult listTopicsResult = ADMIN.listTopics();
        System.out.println(listTopicsResult.names().get());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        list();
        ADMIN.close();
    }

}