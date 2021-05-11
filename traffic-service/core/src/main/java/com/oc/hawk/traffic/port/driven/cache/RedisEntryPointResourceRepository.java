package com.oc.hawk.traffic.port.driven.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfig;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointConfigID;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointDescription;
import com.oc.hawk.traffic.entrypoint.domain.model.entrypoint.EntryPointResourceRepository;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpMethod;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpPath;
import com.oc.hawk.traffic.entrypoint.domain.model.httpresource.HttpResource;

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
        loadIntoList(configListGet,HTTP_METHOD_GET.toLowerCase());
        //POST LIST
        List<EntryPointConfig> configListPost = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_POST, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadIntoList(configListPost,HTTP_METHOD_POST.toLowerCase());
        //PUT LIST
        List<EntryPointConfig> configListPut = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_PUT, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadIntoList(configListPut,HTTP_METHOD_PUT.toLowerCase());
        //DELETE LIST
        List<EntryPointConfig> configListDelete = configList.stream().filter(obj -> StringUtils.equals(HTTP_METHOD_DELETE, obj.getHttpResource().getMethod().name())).collect(Collectors.toList());
        loadIntoList(configListDelete,HTTP_METHOD_DELETE.toLowerCase());
        //HASH
        loadEntryPointConfig(configList);
    }
    
    private void loadIntoList(List<EntryPointConfig> configMethodList,String method) {
        List<String> pathList = configMethodList.stream().map(obj -> obj.getHttpResource().getPath().getPath()).collect(Collectors.toList());
        if(Objects.isNull(pathList) || pathList.isEmpty()) {
            return ;
        }
        String key = new StringBuilder().append(ENTRYPOINT_PATH_KEY).append(method).append("_").toString();
        List<String> pathKeyList = pathList.stream().map(obj -> key+obj).collect(Collectors.toList());
        String listKey = ENTRYPOINT_PATH_LIST + method;
        redisTemplate.opsForList().leftPushAll(listKey, pathKeyList);
    }
    
    private void loadEntryPointConfig(List<EntryPointConfig> configList) {
        configList.forEach(item -> {
            String path = item.getHttpResource().getPath().getPath();
            String method = item.getHttpResource().getMethod().name().toLowerCase();
            String key = new StringBuilder()
                    .append(ENTRYPOINT_PATH_KEY)
                    .append(method)
                    .append("_")
                    .append(path)
                    .toString();
            Map<String,String> hashMap = getHash(item.getConfigId().getId(),path,item.getDescription().getName());
            redisTemplate.opsForHash().putAll(key, hashMap);
        });
    }
    
    @Override
    public void deleteAllEntryPointResource(List<EntryPointConfig> configList) {
        //delete GET List
        redisTemplate.delete(ENTRYPOINT_PATH_LIST+HTTP_METHOD_GET.toLowerCase());
        //delete POST List
        redisTemplate.delete(ENTRYPOINT_PATH_LIST+HTTP_METHOD_POST.toLowerCase());
        //delete PUT List
        redisTemplate.delete(ENTRYPOINT_PATH_LIST+HTTP_METHOD_PUT.toLowerCase());
        //delete DELETE List
        redisTemplate.delete(ENTRYPOINT_PATH_LIST+HTTP_METHOD_DELETE.toLowerCase());
        //delete HASH
        List<String> keyList = new ArrayList<>();
        configList.forEach(item -> {
            String path = item.getHttpResource().getPath().getPath();
            String method = item.getHttpResource().getMethod().name().toLowerCase();
            String key = new StringBuilder()
                    .append(ENTRYPOINT_PATH_KEY)
                    .append(method)
                    .append("_")
                    .append(path)
                    .toString();
            keyList.add(key);
        });
        
        redisTemplate.delete(keyList);
    }
    
    private Map<String,String> getHash(Long id,String path,String name) {
        Map<String,String> hashMap = new HashMap<>(3);
        hashMap.put("id", String.valueOf(id));
        hashMap.put("path", path);
        hashMap.put("name", name);
        return hashMap;
    }

    @Override
    public EntryPointConfig findByPathAndMethod(HttpPath httpPath, HttpMethod httpMethod) {
        String pathKey = getPathKey(httpPath.getPath(),httpMethod.name().toLowerCase());
        
        List<String> pathList = getPathList(httpMethod);
        List<String> filterPathList = pathList.stream().filter(item -> StringUtils.equals(item, pathKey)).collect(Collectors.toList());
        if(Objects.isNull(filterPathList) || filterPathList.isEmpty()) {
            return null;
        }
        Map<String,String> entryMap = getEntryMap(pathKey);
        String entryPointId = (String)entryMap.get("id");
        if(StringUtils.isEmpty(entryPointId)) {
            return null;
        }
        return EntryPointConfig.builder()
                .configId(new EntryPointConfigID(Long.parseLong(entryPointId)))
                .description(new EntryPointDescription(entryMap.get("name"),null))
                .build();
    }
    
    @Override
    public List<EntryPointConfig> findByMethodAndRestfulPath(HttpMethod httpMethod) {
        List<String> pathList = getPathList(httpMethod);
        List<String> filterPathList = pathList.stream().filter(item -> item.contains("{") && item.contains("}")).collect(Collectors.toList());
        List<EntryPointConfig> configList = filterPathList.stream().map(item -> {
            String pathKey = getPathKey(item, httpMethod.name());
            Map<String,String> entryMap = getEntryMap(pathKey);
            String entryPointId = entryMap.get("id");
            EntryPointConfigID configId = null;
            if(!StringUtils.isEmpty(entryPointId)) {
                configId = new EntryPointConfigID(Long.parseLong(entryPointId));
            }
            return EntryPointConfig
                    .builder()
                    .configId(configId)
                    .description(new EntryPointDescription(entryMap.get("name"), null))
                    .httpResource(new HttpResource(new HttpPath(item),httpMethod))
                    .build();
        }).collect(Collectors.toList());
        return configList;
    }
    
    private List<String> getPathList(HttpMethod httpMethod){
        String method = httpMethod.name().toLowerCase();
        String listKey = ENTRYPOINT_PATH_LIST + method;
        return redisTemplate.opsForList().range(listKey, 0, -1);
    }
    
    private Map<String,String> getEntryMap(String pathKey) {
        Map<Object,Object> entryMap = redisTemplate.opsForHash().entries(pathKey);
        return entryMap.entrySet().stream().collect(Collectors.toMap(e -> (String)e.getKey() , e -> (String)e.getValue()));
    }
    
    private String getPathKey(String path,String method) {
        return new StringBuilder()
                .append(ENTRYPOINT_PATH_KEY)
                .append(method)
                .append("_")
                .append(path)
                .toString();
    }
    
    @Override
    public void addConfig(EntryPointConfig config) {
        //add LIST
        String pathKey = getPathKey(config.getHttpResource().getPath().getPath(),config.getHttpResource().getMethod().name());
        String listKey = ENTRYPOINT_PATH_LIST + config.getHttpResource().getMethod().name().toLowerCase();
        redisTemplate.opsForList().leftPush(listKey, pathKey);
        //add HASH
        Map<String,String> hashMap = getHash(config.getConfigId().getId(),config.getHttpResource().getPath().getPath(),config.getDescription().getName());
        redisTemplate.opsForHash().putAll(pathKey, hashMap);
    }

    @Override
    public void deleteConfig(EntryPointConfig config) {
        //delete HASH
        String pathKey = getPathKey(config.getHttpResource().getPath().getPath(),config.getHttpResource().getMethod().name());
        redisTemplate.delete(pathKey);
        //delete LIST key
        String listKey = ENTRYPOINT_PATH_LIST + config.getHttpResource().getMethod().name().toLowerCase();
        redisTemplate.opsForList().remove(listKey, 0, pathKey);
    }
    
}
