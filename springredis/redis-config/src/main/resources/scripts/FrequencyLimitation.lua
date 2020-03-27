-- FrequencyLimitation
--[[
思路:
    1. 获取list长度
    2. 不超过限制, 就lpush, 并返回true
    3. 超过限制, 获取最后列表元素的时间戳
    4. 如果时间差,小于限制周期, 返回false
    5. 如果时间差大于限制周期, lpush并移除最后一个元素, 返回true

    Lua 算数运算符 会尝试将字符串转换成数字
        比较运算符 不会转换

    注意: 因为value设置的是jack序列化, 所以value的类型是String

    可以设置过期时间, 也可以利用redis的LRU算法, 自动删除过期的键, 最好还是手动过期

    并发效果, 并不能真正防止并发, 毫秒级别下的无法控制
--]]
local len = redis.call('LLEN', KEYS[1])
local now = tonumber(ARGV[1])
local period = tonumber(ARGV[2]) * 1000
local count = tonumber(ARGV[3])
if(len < count)
then
   redis.call('rpush', KEYS[1], now)
   return true
else
    local earlyTimeStamp = tonumber(redis.call('lrange', KEYS[1], 0, 0)[1])
    local timeDifference = now - earlyTimeStamp
    if(timeDifference < period)
    then
        return false
    else
        redis.call('lpop', KEYS[1])
        return true
    end
end