package com.oc.hawk.traffic.port.driven.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointResourceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisEntryPointResourceRepository implements EntryPointResourceRepository {
    
    private final RedisTemplate<String, String> redisTemplate;
    private static final String HTTP_METHOD_GET = "GET";
    private static final String HTTP_METHOD_POST = "POST";
    private static final String HTTP_METHOD_PUT = "PUT";
    private static final String HTTP_METHOD_DELETE = "DELETE";
    
    private static final String ENTRYPOINT_PATH_LIST = "entrypoint_path_list_";
    private static final String ENTRYPOINT_PATH_KEY = "entrypoint_path_key_";
    
    @Override
    public void loadEntryPointResource(List<EntryPointConfig> configList) {
        //GET LIST
        List<EntryPointConfig> configListGet = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_GET, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadGetList(configListGet);
        //POST LIST
        List<EntryPointConfig> configListPost = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_POST, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadPostList(configListPost);
        //PUT LIST
        List<EntryPointConfig> configListPut = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_PUT, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadPutList(configListPut);
        //DELETE LIST
        List<EntryPointConfig> configListDelete = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_DELETE, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadDeleteList(configListDelete);
        //HASH
        loadEntryPointConfig(configList);
    }
    
    private void loadGetList(List<EntryPointConfig> configListGet) {
        List<String> pathGetList = configListGet.stream().map(obj -> obj.getHttpResource().getPath().getPath()).collect(Collectors.toList());
        StringBuilder key = new StringBuilder().append(ENTRYPOINT_PATH_KEY).append(HTTP_METHOD_GET.toLowerCase()).append("_");
        List<String> pathKeyGetList = pathGetList.stream().map(obj -> key.append(obj).toString()).collect(Collectors.toList());
        String listKeyGet = ENTRYPOINT_PATH_LIST + HTTP_METHOD_GET.toLowerCase();
        redisTemplate.opsForList().leftPushAll(listKeyGet, pathKeyGetList);
    }
    
    private void loadPostList(List<EntryPointConfig> configListPost) {
        List<String> pathPostList = configListPost.stream().map(obj -> obj.getHttpResource().getPath().getPath()).collect(Collectors.toList());
        StringBuilder key = new StringBuilder().append(ENTRYPOINT_PATH_KEY).append(HTTP_METHOD_POST.toLowerCase()).append("_");
        List<String> pathKeyPostList = pathPostList.stream().map(obj -> key.append(obj).toString()).collect(Collectors.toList());
        String listKeyPost = ENTRYPOINT_PATH_LIST + HTTP_METHOD_POST.toLowerCase();
        redisTemplate.opsForList().leftPushAll(listKeyPost, pathKeyPostList);
    }
    
    private void loadPutList(List<EntryPointConfig> configListPut) {
        List<String> pathPutList = configListPut.stream().map(obj -> obj.getHttpResource().getPath().getPath()).collect(Collectors.toList());
        StringBuilder key = new StringBuilder().append(ENTRYPOINT_PATH_KEY).append(HTTP_METHOD_PUT.toLowerCase()).append("_");
        List<String> pathKeyPutList = pathPutList.stream().map(obj -> key.append(obj).toString()).collect(Collectors.toList());
        String listKeyPut = ENTRYPOINT_PATH_LIST + HTTP_METHOD_PUT.toLowerCase();
        redisTemplate.opsForList().leftPushAll(listKeyPut, pathKeyPutList);
    }
    
    private void loadDeleteList(List<EntryPointConfig> configListDelete) {
        List<String> pathDeleteList = configListDelete.stream().map(obj -> obj.getHttpResource().getPath().getPath()).collect(Collectors.toList());
        StringBuilder key = new StringBuilder().append(ENTRYPOINT_PATH_KEY).append(HTTP_METHOD_PUT.toLowerCase()).append("_");
        List<String> pathKeyDeleteList = pathDeleteList.stream().map(obj -> key.append(obj).toString()).collect(Collectors.toList());
        String listKeyDelete = ENTRYPOINT_PATH_LIST + HTTP_METHOD_DELETE.toLowerCase();
        redisTemplate.opsForList().leftPushAll(listKeyDelete, pathKeyDeleteList);
    }
    
    private void loadEntryPointConfig(List<EntryPointConfig> configList) {
        configList.forEach(item -> {
            String path = item.getHttpResource().getPath().getPath();
            String key = new StringBuilder()
                    .append(ENTRYPOINT_PATH_KEY)
                    .append(HTTP_METHOD_PUT.toLowerCase())
                    .append("_")
                    .append(path)
                    .toString();
            Map<String,Object> hashMap = new HashMap<String,Object>();
            hashMap.put("id", item.getConfigId().getId());
            hashMap.put("path", path);
            hashMap.put("name", item.getDescription().getName());
            redisTemplate.opsForHash().putAll(key, hashMap);
        });
    }
    
}
