package com.oc.hawk.common.spring.support.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Objects;

@Slf4j
public class RedisLock implements DistributedLock {
    /**
     * 失效时间 默认60s
     */
    public static final int EXPIRE = 60;
    /**
     * 5秒内去设置redis
     */
    private static final long TIME_OUT = 5;

    /**
     * redis中锁住存储的值
     */
    private final String value;
    /**
     * 存储在redis中的key
     */
    private final String key;
    private StringRedisTemplate redisTemplate;


    public RedisLock(String key, StringRedisTemplate stringRedisTemplate) {
        String instanceId = RandomStringUtils.randomAlphabetic(10);
        this.redisTemplate = stringRedisTemplate;
        this.key = "hawk:platform:lock:" + key;

        value = System.currentTimeMillis() + instanceId;
    }

    private boolean lockedBySelf() {
        return Objects.equals(getRedisTemplate().opsForValue().get(key), this.value);
    }

    /**
     * 持续一段时间获取锁
     */
    @Override
    public boolean lock() {
        return lock(TIME_OUT);
    }

    private synchronized boolean lock(long timeout) {
        if (this.lockedBySelf()) {
            return true;
        }
        if (timeout == 0) {
            return this.tryLock();
        }

        long nano = System.nanoTime();
        timeout *= 1000 * 1000 * 1000;

        try {
            while ((System.nanoTime() - nano) < timeout) {
                if (this.tryLock()) {
                    return true;
                }
                Thread.sleep(3, RandomUtils.nextInt(1000));
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("", e);
            }
        }
        return false;
    }


    @Override
    public boolean tryLock() {
        try {
            log.debug("Add RedisLock[" + key + "].");
            return this.setNx(key, value, EXPIRE);
        } catch (Exception e) {
            log.error("locked error.", e);
        }
        return false;
    }

    @Override
    public void unlock() {
        if (lockedBySelf()) {
            getRedisTemplate().delete(key);
            log.debug("Release RedisLock[" + key + "].");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Boolean setNx(final String key, final String value, long timeout) {
        RedisSerializer keySerializer = getRedisTemplate().getKeySerializer();
        RedisSerializer valueSerializer = getRedisTemplate().getValueSerializer();
        byte[] keys = keySerializer.serialize(key);
        byte[] values = valueSerializer.serialize(value);

        if (Objects.nonNull(keys) && Objects.nonNull(values)) {

            return getRedisTemplate()
                .execute((RedisCallback<Boolean>) connection ->
                    connection.set(keys, values, Expiration.seconds(timeout), RedisStringCommands.SetOption.SET_IF_ABSENT));
        }
        return false;
    }

    private RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }


    @Override
    public void close() {
        this.unlock();
    }
}

