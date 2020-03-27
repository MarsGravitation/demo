package com.microwu.cxd.dubbo;

import com.microwu.cxd.service.ProviderService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException {

        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("consumer.xml");
        applicationContext.start();

        ProviderService providerService = (ProviderService) applicationContext.getBean("providerService");
        String helloWorld = providerService.sayHello("Hello World");
        System.out.println(helloWorld);
        System.in.read();

    }
}
