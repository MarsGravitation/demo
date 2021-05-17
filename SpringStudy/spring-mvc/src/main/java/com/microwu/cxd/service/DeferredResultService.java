package com.microwu.cxd.service;

import com.microwu.cxd.domain.DeferredResultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Author: chengxudong             chengxd2@lenovo.com
 * Date:    2021/5/13  9:44
 * Copyright:      lenovo			https://www.lenovo.com.cn/
 */
@Service
public class DeferredResultService {

    private Map<String, Consumer<DeferredResultResponse>> taskMap;

    public DeferredResultService() {
        taskMap = new ConcurrentHashMap<>();
    }

    /**
     * 将请求 id 与 setResult 映射
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/13     9:59
     *
     * @param requestId
     * @param deferredResult
     * @return void
     */
    public void process(String requestId, DeferredResult<DeferredResultResponse> deferredResult) {
        // 请求超时的回调函数
        deferredResult.onTimeout(() -> {
            taskMap.remove(requestId);
            DeferredResultResponse deferredResultResponse = new DeferredResultResponse();
            deferredResultResponse.setCode(HttpStatus.REQUEST_TIMEOUT.value());
            deferredResultResponse.setMsg(DeferredResultResponse.Msg.TIMEOUT.getDesc());
            deferredResult.setResult(deferredResultResponse);
        });

        Optional.ofNullable(taskMap)
                .filter(t -> !t.containsKey(requestId))
                .orElseThrow(() -> new IllegalArgumentException(String.format("requestId = %s is existing", requestId)));

        // 这里的 consumer，就相当于是传入的 DeferredResult 对象的地址
        // 所以下面 settingResult 方法中的 taskMap.get(requestId)" 就是 controller 层创建的对象
        taskMap.putIfAbsent(requestId, deferredResult::setResult);
    }

    /**
     * 这里相当于异步的操作方法
     * 设置 DeferredResult 对象的 setResult 方法
     *
     * @author   chengxudong             chengxd2@lenovo.com
     * @date 2021/5/13     10:05
     *
     * @param requestId
     * @param deferredResultResponse
     * @return void
     */
    public void settingResult(String requestId, DeferredResultResponse deferredResultResponse) {
        if (taskMap.containsKey(requestId)) {
            Consumer<DeferredResultResponse> deferredResultResponseConsumer = taskMap.get(requestId);
            // 这里相当于 DeferredResult 对象的 setResult 方法
            deferredResultResponseConsumer.accept(deferredResultResponse);
            taskMap.remove(requestId);
        }

    }
}
