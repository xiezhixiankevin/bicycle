package com.shixun.bicycle.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <Description> RedisMapTools
 *
 * @author 26802
 * @version 1.0
 * @see com.shixun.bicycle.utils
 */
@Slf4j
@Component
public class RedisMapTools {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 删除key
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 是否存在key
     * @param key
     * @return
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    //------------------------------------------ Map的操作 -----------------------------------------------

    /**
     *  保存map值到指定key
     */
    public void hput(String key, String field, Object object){
        redisTemplate.opsForHash().put(key, field, object);
    }

    /**
     * 保存多个key-value的map
     */
    public <T> void hPutAll(String key, Map<String,T> map){
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取存储在哈希表中指定字段的值
     * @param key
     * @param field
     * @return
     */
    public <T> T hGet(String key, String field, Class<T> clazz ) {
        if (!hasKey(key)) {
            return null;
        }
        String mapJson = JSON.toJSONString(redisTemplate.opsForHash().get(key, field));
        return JSON.parseObject(mapJson, clazz);
    }

    /**
     * 删除一个或多个哈希表字段
     *
     * @param key
     * @param fields
     * @return
     */
    public Long hDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }


    /**
     * 获取一个map集
     * @param key 名
     * @param clazz 返回的map-value的值类型
     * @param <E>
     * @return
     */
    public <E> Map<String, E> hGetAll(String key, Class<E> clazz) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        if(entries==null || entries.size()==0){
            return Collections.emptyMap();
        }
        Map<String, E> resultMap = new LinkedHashMap<>(entries.size());
        for(Map.Entry<Object,Object> entry : entries.entrySet()){
            E valueObj = JSON.parseObject(JSON.toJSONString(entry.getValue()), clazz );
            String hashKey = JSON.toJSONString(entry.getKey());
            if (hashKey == null) {
                return null;
            }
            hashKey = hashKey.replace("\"", "");
            resultMap.put(hashKey, valueObj);
        }
        return resultMap;
    }
}
