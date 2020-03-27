package com.microwu.cxd.spring;

import com.microwu.cxd.spring.heart.HeartBeatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class NettyClientApplication implements CommandLineRunner {
    @Autowired
    private HeartBeatClient heartBeatClient;

    public static void main(String[] args) {

        SpringApplication.run(NettyClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        heartBeatClient.connect("127.0.0.1", 8080);
    }
}
