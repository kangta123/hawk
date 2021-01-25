package com.oc.hawk.kubernetes.keepalive.port.driven.cache;

import com.oc.hawk.kubernetes.keepalive.application.KubernetesEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisKubernetesEventRepository implements KubernetesEventRepository {
    private final String KUBERNETES_EVENT_RESOURCE_VERSION = "container:event:versions:%s";
    private final String KUBERNETES_EVENT_RESOURCE_CURRENT_VERSION = "container:version:current";

    private final RedisTemplate<String, String> stringRedisTemplate;

    @Override
    public boolean isReceived(String resourceVersion) {
        return !setNx(String.format(KUBERNETES_EVENT_RESOURCE_VERSION, resourceVersion), resourceVersion, 60 * 10);
    }

    @Override
    public String loadEventResourceVersion() {
        return stringRedisTemplate.opsForValue().get(KUBERNETES_EVENT_RESOURCE_CURRENT_VERSION);
    }

    @Override
    public void updateEventResourceVersion(String version) {
        stringRedisTemplate.opsForValue().set(KUBERNETES_EVENT_RESOURCE_CURRENT_VERSION, version);
    }

    @Override
    public void dropEventResourceVersion() {
        stringRedisTemplate.delete(KUBERNETES_EVENT_RESOURCE_CURRENT_VERSION);
    }

    private Boolean setNx(final String key, final String value, long timeout) {
        RedisSerializer keySerializer = stringRedisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = stringRedisTemplate.getValueSerializer();
        byte[] keys = keySerializer.serialize(key);
        byte[] values = valueSerializer.serialize(value);

        if (Objects.nonNull(keys) && Objects.nonNull(values)) {
            return stringRedisTemplate
                .execute((RedisCallback<Boolean>) connection ->
                    connection.set(keys, values, Expiration.seconds(timeout), RedisStringCommands.SetOption.SET_IF_ABSENT));
        }
        return false;
    }

}

