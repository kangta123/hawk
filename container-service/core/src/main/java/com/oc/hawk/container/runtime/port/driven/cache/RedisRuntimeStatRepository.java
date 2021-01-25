package com.oc.hawk.container.runtime.port.driven.cache;

import com.google.common.collect.Maps;
import com.oc.hawk.container.domain.model.runtime.stat.RuntimeStatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisRuntimeStatRepository implements RuntimeStatRepository {
    public static final String PROJECT_TOTAL_COUNT_KEY = "hawk:project:runtime:total_count";
    public static final String PROJECT_COUNT_OF_RUNNING_KEY = "hawk:project:running_count_of_project";
    private final StringRedisTemplate redisTemplate;


    @Override
    public void reset(Map<Long, Integer> projectRuntimeCount) {
        Integer total = projectRuntimeCount.values().stream().reduce(Math::addExact).orElse(0);
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().set(PROJECT_TOTAL_COUNT_KEY, String.valueOf(total));
                projectRuntimeCount.forEach((projectId, count) -> {
                    operations.opsForHash().put(PROJECT_COUNT_OF_RUNNING_KEY, String.valueOf(projectId), String.valueOf(count));
                });

                return null;
            }
        });
    }

    @Override
    public int getTotalRuntimeCount() {
        String c = StringUtils.defaultIfEmpty(redisTemplate.opsForValue().get(PROJECT_TOTAL_COUNT_KEY), "0");
        return Integer.parseInt(c);
    }

    @Override
    public Map<Long, Integer> getRuntimeCountByProject(List<Long> projectIds) {
        if (CollectionUtils.isEmpty(projectIds)) {
            return Maps.newHashMap();
        }
        List<String> collect = projectIds.stream().map(String::valueOf).collect(Collectors.toList());
        List<String> counts = redisTemplate.<String, String>opsForHash().multiGet(PROJECT_COUNT_OF_RUNNING_KEY, collect);
        Map<Long, Integer> result = Maps.newHashMapWithExpectedSize(projectIds.size());
        for (int i = 0; i < projectIds.size(); i++) {
            String value = counts.get(i);
            if (StringUtils.isNotEmpty(value)) {
                result.put(projectIds.get(i), Integer.parseInt(value));
            }
        }
        return result;
    }

    private Map<String, String> hget(String key) {
        return redisTemplate.execute((RedisCallback<Map<String, String>>) con -> {
            byte[] keyBytes = key.getBytes();
            Map<byte[], byte[]> result = con.hGetAll(keyBytes);
            if (CollectionUtils.isEmpty(result)) {
                return new HashMap<>(0);
            }

            Map<String, String> values = new HashMap<>(result.size());
            result.forEach((k, v) -> values.put(new String(k), new String(v)));
            return values;
        });
    }


}
