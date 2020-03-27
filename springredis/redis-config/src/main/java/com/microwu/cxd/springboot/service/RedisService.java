package com.microwu.cxd.springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * Author:         chengxudong             chengxudong@microwu.com
 * Date:           2019/8/14   17:48
 * Copyright       北京小悟科技有限公司       http://www.microwu.com
 * Update History:
 * Author        Time            Content
 */
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置数据库
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/15  10:23
     *
     * @param
     * @return  void
     */
    public void set() {
        redisTemplate.opsForValue().set("name", "cxd");
    }

    /**
     * 事务 - Redis提供事务, 通过multi, exec, discard等命令,
     *      但是, redisTemplate不保证在具有相同连接的事务中执行所有操作
     *
     *      spring提供了SessionCallback, 在需要执行多个操作的时候使用的接口connection
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/15  10:23
     *
     * @param
     * @return  void
     */
    public void transaction() {
        List<Object> list = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                redisOperations.opsForValue().setIfAbsent("name", "cxd");
                return redisOperations.exec();
            }
        });
        // 返回值是一个集合, 对应每一条命令的返回值
        System.out.println(list.get(0));

    }

    /**
     * 管道传输
     *      Redis支持管道流操作, 包括向服务器发送多个命令而无需等待回复
     *      然后在一个步骤中读取回复. 但需要连续发送多个命令时, 流水线操作可以提高性能
     *
     *      如果你不关心流水线操作的结果, 可以使用标准的execute方法, 传递true的pipeline参数
     *      所述executePipelined 方法在管道中运行提供的RedisCallback 或者 SessionCallback, 并返回结果
     *
     *      这里需要进行序列化设置
     *
     *      可以管道传输 + 事务, 因为都是使用一个connection
    *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/15  10:53
     *
     * @param
     * @return  void
     */
    public void pipeline() {
        List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) {
                connection.set("age".getBytes(), "18".getBytes());
                connection.get("age".getBytes());
                // 返回值必须为null, 此值被丢弃, 返回流水线命令结果
                return null;
            }
        });
        System.out.println(list.get(0) + ": " + list.get(1));

    }

    /**
     * Redis2.6及更高版本通过eval和evalsha命令为执行Lua脚本提供支持
     *  Spring为脚本执行提供了高级抽象, 处理序列化并自动使用Redis脚本缓存
     *
     *  可以通过调用RedisTemplate 来运行提供的脚本. 默认情况下, ScriptExecutor负责序列化
     *  提供的键和参数并反序列化脚本结果. 这通过模版的键和值序列化器完成. 还有一个额外的重载,
     *  允许您为脚本参数和结果传递自定义的序列化程序. 默认ScriptExecutor通过检索脚本的SHA1
     *  并尝试首先运行evalsha来优化性能. 如果脚本尚未出现在Redis脚本缓存中, 则返回到eval
     *
     *  checkandset是Redis脚本的理想用例. 它要求原子方式运行一组命令, 并且一个命令的行为受
     *  另一个命令结果的影响
     *
     *  EAVL
     *      第一个参数: Lua脚本
     *      第二个参数: 表示脚本中所用到的redis key
     *      以后的参数: redis key 通过 KEYS[i] 获取
     *                  附加参数 通过 ARGV[i] 获取
     *                  两个数组下表都是从1 开始的
     *
     *  配置脚本为单例, 避免每次脚本执行时重新计算脚本的SHA1
     *
     *  脚本可以SessionCallback 作为事务或管道的一部分在内部运行
     *
     *          String scriptSource = "local current = redis.call('GET', KEYS[1])\n" +
     *                 "if(current == ARGV[1])\n" +
     *                 "    then redis.call('SET', KEYS[1], ARGV[2])\n" +
     *                 "    return true\n" +
     *                 "end\n" +
     *                 "return false";
     *         RedisScript<Boolean> script = RedisScript.of(scriptSource, Boolean.class);
     *         Boolean execute = redisTemplate.execute(script, Collections.singletonList("name"), "cxd", "cxj");
     *         System.out.println(execute);
     *
     *         ResourceScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource(("scripts/checkandset.lua")));
     *         DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<Boolean>();
     *         redisScript.setScriptSource(scriptSource);
     *         redisScript.setResultType(Boolean.class);
     *         Boolean execute = redisTemplate.execute(redisScript, Collections.singletonList("name"), "cxd", "cxj");
     *         System.out.println(execute);
     *
     *                 ResourceScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource(("scripts/test01.lua")));
     *         DefaultRedisScript<List> redisScript = new DefaultRedisScript<List>();
     *         redisScript.setScriptSource(scriptSource);
     *         redisScript.setResultType(List.class);
     *         List<String> strings = new ArrayList<>(2);
     *         strings.add("key1");
     *         strings.add("key2");
     *         List<String> list = redisTemplate.execute(redisScript, strings, "first", "second");
     *         list.stream().forEach(System.out::println);
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/15  11:37
     *
     * @param
     * @return  void
     */
    public void script() {
        ResourceScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource(("scripts/lock.lua")));
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(scriptSource);
        redisScript.setResultType(Boolean.class);
        Boolean execute = redisTemplate.execute(redisScript, Collections.singletonList("name"), "cxd");
        System.out.println(execute);

    }

    /**
     * redis实现限制
     *
     * @author   chengxudong               chengxudong@microwu.com
     * @date    2019/8/16  10:11
     *
     * @param   	actionKey
     * @param 		mobile
     * @param 		period
     * @param 		maxCount
     * @return  java.lang.Boolean
     */
    public Boolean frequencyLimitation(String actionKey, String mobile, int period, int maxCount) {
        // 每一个手机号生成一个 key
        String key = new StringBuilder().append(actionKey).append(":").append(mobile).toString();
        // 获取当前时间戳
        long timeStampMills = Instant.now().toEpochMilli();
        ResourceScriptSource scriptSource = new ResourceScriptSource(new ClassPathResource(("scripts/frequencyLimitation.lua")));
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(scriptSource);
        redisScript.setResultType(Boolean.class);
        Boolean execute = redisTemplate.execute(redisScript, Collections.singletonList(key), timeStampMills, period, maxCount);
        return execute;
    }
}