package com.microwu.cxd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class MultithreadApplication
{
    public static void main( String[] args ) throws InterruptedException {
        SpringApplication.run(MultithreadApplication.class, args);
    }
}
