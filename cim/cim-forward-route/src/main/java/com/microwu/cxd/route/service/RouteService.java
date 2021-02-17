package com.microwu.cxd.route.service;

import com.microwu.cxd.common.route.ChatReqVO;
import com.microwu.cxd.common.route.CimServerResVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:30
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class RouteService {

    @Autowired
    private AccountService accountService;

    public void group(ChatReqVO chatReqVO) {
        // 1. 获取所有的推送列表
        Map<Long, CimServerResVO> serverResVOMap = accountService.loadRouteRelated();

        // 2. 推送消息
        serverResVOMap.entrySet().stream().filter(entry -> !entry.getKey().equals(chatReqVO.getUserId())).forEach(entry -> {
            Long userId = entry.getKey();
            ChatReqVO reqVO = new ChatReqVO();
            reqVO.setUserId(userId);
            reqVO.setMessage(chatReqVO.getMessage());
            accountService.pushMsg(entry.getValue(), chatReqVO.getUserId(), reqVO);
        });

    }
}