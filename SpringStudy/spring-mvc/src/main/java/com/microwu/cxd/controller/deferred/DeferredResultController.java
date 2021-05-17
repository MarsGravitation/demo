package com.microwu.cxd.controller.deferred;

import com.microwu.cxd.domain.DeferredResultResponse;
import com.microwu.cxd.service.DeferredResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * DeferredResult
 *
 * 为什么使用 DeferredResult
 *  API 接口需要在指定时间内将异步操作的记过同步返回给前端时；
 *  Controller 处理耗时任务，并且需要耗时任务的返回结果时；
 *
 *  当一个请求到达 API 接口，如果该 API 接口的 return 返回值时 DeferredResult，在没有超时或者 deferredResult
 *  对象设置 setResult 时，接口返回，但是 Servlet 容器线程会结束，DeferredResult 另起线程来进行结果处理（即这种操作提升了
 *  服务短时间的吞吐能力），并 setResult，如此一来，这个请求不会占用服务连接池太久，如果超时或设置 setResult，接口会立即返回
 *
 * 使用 DeferredResult 的流程：
 *  1. 浏览器发起异步请求
 *  2. 请求到达服务端被挂起
 *  3. 向浏览器进行响应，分为两种情况：
 *      3.1 调用 DeferredResult.setResult()，请求被唤醒，返回结果
 *      3.2 超时，返回一个你设定的结果
 *   4. 浏览器得到响应，再次重复 1，处理此次响应结果
 *
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/13  9:38
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@RestController
@RequestMapping("/deferred-result")
public class DeferredResultController {

    @Autowired
    private DeferredResultService deferredResultService;

    /**
     * 为了方便测试，简单模拟一个
     * 多个请求用同一个 requestID 会出问题
     */
    private final String requestId = "haha";

    @GetMapping("/get")
    public DeferredResult<DeferredResultResponse> get(@RequestParam(value = "timeout", required = false, defaultValue = "10000") Long timeout) {
        DeferredResult<DeferredResultResponse> deferredResult = new DeferredResult<DeferredResultResponse>(timeout);

        deferredResultService.process(requestId, deferredResult);

        return deferredResult;
    }

    @GetMapping("/result")
    public String settingResult(@RequestParam(value = "desired", required = false, defaultValue = "成功") String desired) {
        DeferredResultResponse deferredResultResponse = new DeferredResultResponse();
        if (DeferredResultResponse.Msg.SUCCESS.getDesc().equals(desired)) {
            deferredResultResponse.setCode(HttpStatus.OK.value());
            deferredResultResponse.setMsg(desired);
        } else {
            deferredResultResponse.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            deferredResultResponse.setMsg(DeferredResultResponse.Msg.FAILED.getDesc());
        }
        deferredResultService.settingResult(requestId, deferredResultResponse);

        return "Done";
    }

}
