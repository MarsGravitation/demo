package com.microwu.cxd.route.service;

import com.microwu.cxd.common.route.ChatReqVO;
import com.microwu.cxd.common.route.CimServerResVO;
import com.microwu.cxd.common.utils.RouteInfoParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.microwu.cxd.route.constants.RouteConstant.ROUTE_PREFIX;

/**
 * Description:
 *
 * @Author: chengxudong             chengxudong@microwu.com
 * Date:       2020/12/10   14:33
 * Copyright:      北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class AccountService {

    @Autowired
    private RedisTemplate redisTemplate;

    public Map<Long, CimServerResVO> loadRouteRelated() {
        final Map<Long, CimServerResVO> routes = new HashMap<>(64);
        // 1. 模糊查询
        Set<String> set = (Set<String>) redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                final HashSet<String> keys = new HashSet<>();
                Cursor<byte[]> scan = redisConnection.scan(new ScanOptions.ScanOptionsBuilder().match(ROUTE_PREFIX + "*").build());
                while (scan.hasNext()) {
                    keys.add(new String(scan.next()));
                }
                return keys;
            }
        });

        set.stream().forEach(s -> {
            long userId = Long.valueOf(s.split(":")[1]);
            String value = (String) redisTemplate.opsForValue().get(s);
            CimServerResVO cimServerResVO = new CimServerResVO(RouteInfoParseUtil.parse(value));
            routes.put(userId, cimServerResVO);
        });
        return routes;
    }

    public void pushMsg(CimServerResVO value, Long userId, ChatReqVO reqVO) {
    }
}