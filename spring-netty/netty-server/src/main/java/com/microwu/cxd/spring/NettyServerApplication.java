package com.microwu.cxd.spring;

import com.microwu.cxd.spring.heart.HeartBeatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {
    @Autowired
//    private TimeServer timeServer;
    private HeartBeatServer heartBeatServer;

    public static void main( String[] args ) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        timeServer.bind(8080);
        heartBeatServer.bind(8080);
    }
}
