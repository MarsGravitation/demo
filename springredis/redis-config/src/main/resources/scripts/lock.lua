-- Redis分布式锁
local value = redis.call('get', KEYS[1])
if(value)
then
    return false
else
    redis.call('set', KEYS[1], ARGV[1])
    return true
end