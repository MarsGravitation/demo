package com.microwu.cxd.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.stream.IntStream;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2019/12/12   10:31
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@RestController
public class HelloController {
    private static final String HELLO_WORLD = "hello, world!";

    /**
     *  MediaType.TEXT_EVENT_STREAM_VALUE 设置这个头部是让flux变成响应式的
     *  如果不设置这个参数，flux就相当于List Array
     * @return
     */
    @GetMapping(value = "/hello",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> hello(){
        return Flux
                .fromStream(IntStream.range(1,20).mapToObj(item ->HELLO_WORLD + item))
                .doOnNext(item -> {
                    try {
                        //等待500毫秒响应一次数据
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
    }

}