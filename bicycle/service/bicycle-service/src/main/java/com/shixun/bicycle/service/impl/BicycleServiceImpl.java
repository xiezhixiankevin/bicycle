package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.BicycleFault;
import cn.itcast.feign.pojo.Location;
import cn.itcast.feign.pojo.TUser;
import cn.itcast.feign.pojo.pack.BicycleUsing;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shixun.bicycle.mapper.BicycleMapper;
import com.shixun.bicycle.pojo.FixInfo;
import com.shixun.bicycle.service.BicycleFaultService;
import com.shixun.bicycle.service.BicycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.bicycle.utils.ApplicationContextProvider;
import com.shixun.bicycle.utils.DistanceUtil;
import com.shixun.bicycle.utils.RedisMapTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
@Service
public class BicycleServiceImpl extends ServiceImpl<BicycleMapper, Bicycle> implements BicycleService {

    @Autowired
    private RedisMapTools redisMapTools;

    private static final String TRAILS_MAP_KEY = "bicycle-trails-map";

    @Override
    public List<Bicycle> listBicyclesByIds(List<Integer> ids) {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.in("id",ids);
        return list(bicycleQueryWrapper);
    }

    @Override
    public List<Bicycle> listFaultBicycles(Integer faultId) {
        if(faultId != null){
            BicycleFaultService bicycleFaultService = ApplicationContextProvider.getBean(BicycleFaultService.class);
            List<BicycleFault> bicycleFaults = bicycleFaultService.listByFaultId(faultId);
            if(bicycleFaults != null){
                List<Integer> ids = new ArrayList<>();
                for (BicycleFault bicycleFault : bicycleFaults) {
                    ids.add(bicycleFault.getBicycleId());
                }
                return listBicyclesByIds(ids);
            }
            return new ArrayList<>();
        }
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("state",Bicycle.FAULT);
        return list(bicycleQueryWrapper);
    }

    @Override
    public List<Bicycle> listSurroundingBicycles(Double jd, Double wd) {

        QueryWrapper<Bicycle>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",Bicycle.FREE);
        List<Bicycle> list = list(queryWrapper);

        List<Bicycle> result = new ArrayList<>();

        for (Bicycle bicycle : list) {
            double distance = DistanceUtil.getDistance(jd,wd,bicycle.getJd(),bicycle.getWd());
            if (distance <= 500.0){
                result.add(bicycle);
            }
        }

        return result;
    }

    @Override
    public Integer addFaultBicycle(Integer bicycleId, Integer faultId) {

        Bicycle bicycle = getById(bicycleId);
        if (bicycle.getState().equals(Bicycle.USING)){
            return 0; // 单车正在使用
        }

        BicycleFaultService bicycleFaultService = ApplicationContextProvider.getBean(BicycleFaultService.class);

        UpdateWrapper<Bicycle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",bicycleId);
        updateWrapper.ne("state",Bicycle.USING);

        bicycle = new Bicycle();
        bicycle.setState(2);
        update(bicycle,updateWrapper);

        bicycleFaultService.save(new BicycleFault(bicycleId,faultId,new Date()));
        return 1;
    }

    @Override
    public Integer fixBicycleFault(FixInfo fixInfo) {

        BicycleFaultService bicycleFaultService = ApplicationContextProvider.getBean(BicycleFaultService.class);
        QueryWrapper<BicycleFault> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bicycle_id",fixInfo.getBicycleId());
        queryWrapper.in("fault_id",fixInfo.getFaultIds());
        bicycleFaultService.remove(queryWrapper);

        // 看看是不是已经完全修好

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bicycle_id",fixInfo.getBicycleId());

        List<BicycleFault> bicycleHaveFaults = bicycleFaultService.list(queryWrapper);
        if(bicycleHaveFaults != null){
            return 1;
        }
        // 完全修好了
        UpdateWrapper<Bicycle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",fixInfo.getBicycleId());

        Bicycle bicycle = new Bicycle();
        bicycle.setState(Bicycle.RUNNABLE);
        update(bicycle,updateWrapper);
        return 1;
    }

    @Override
    public Integer openLock(Integer bicycleId, TUser user) {
        //1.判断单车是否可使用，可使用则解锁，修改数据库字段
        Bicycle bicycle = getById(bicycleId);
        if (!bicycle.getState().equals(Bicycle.FREE)){
            return 0;
        }
        bicycle.setState(Bicycle.USING);
        updateById(bicycle);
        //2.将单车信息放入redis缓存
        BicycleUsing bicycleUsing = new BicycleUsing();
        bicycleUsing.setBicycle(bicycle);
        bicycleUsing.setUserEmail(user.getUserEmail());
        bicycleUsing.setUserName(user.getUserName());
        bicycleUsing.setUserId(user.getUserId());
        bicycleUsing.getTrails().add(new Location(bicycle.getJd(),bicycle.getWd()));

        String jsonString = JSONObject.toJSONString(bicycleUsing);
        String key = "bicycle" + bicycle.getId() + "-" + "user" + user.getUserId();

        redisMapTools.hput(TRAILS_MAP_KEY,key,jsonString);

        return 1;
    }

    @Override
    public Integer Lock(Bicycle bicycle,TUser user) {
        //1 判断单车是否正在使用
        String key = "bicycle" + bicycle.getId() + "-" + "user" + user.getUserId();
        String jsonString = redisMapTools.hGet(TRAILS_MAP_KEY, key, String.class);
        if (jsonString == null){
            // 单车没有正在使用
            return 0;
        }
        BicycleUsing bicycleUsing = JSONObject.parseObject(jsonString, BicycleUsing.class);
        //2 判断单车是不是当前人使用
        if (!bicycleUsing.getUserId().equals(user.getUserId())){
            return 2;
        }
        //3 删除redis缓存，修改数据库
        redisMapTools.hDelete(TRAILS_MAP_KEY,key);
        bicycleUsing.getBicycle().setState(Bicycle.FREE);
        updateById(bicycleUsing.getBicycle());
        return 1;
    }

    @Override
    public List<BicycleUsing> getTrails() {
        //获取所有单车信息
        List<BicycleUsing> list = new ArrayList<>();
        Map<String, String> bicycleInfoMap = redisMapTools.hGetAll(TRAILS_MAP_KEY, String.class);
        for (String jsonString:bicycleInfoMap.values()){
            BicycleUsing bicycleUsing = JSONObject.parseObject(jsonString, BicycleUsing.class);
            list.add(bicycleUsing);
        }
        return list;
    }

    @Override
    public void addRunnableBicycles(List<Integer> list) {
        UpdateWrapper<Bicycle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",list);
        updateWrapper.eq("state",Bicycle.RUNNABLE);

        Bicycle bicycle = new Bicycle();
        bicycle.setState(Bicycle.FREE);
        update(bicycle,updateWrapper);
    }

    @Override
    public List<Bicycle> getFixedBicycles() {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("state",Bicycle.RUNNABLE);
        return list(bicycleQueryWrapper);
    }

    @Override
    public void addBicycles(List<Bicycle> bicycles) {
        saveBatch(bicycles);
    }

    @Override
    public Object postBicycleTrails(Bicycle bicycle, TUser user) {
        //1 从redis缓存拿到bicycle信息
        String key = "bicycle" + bicycle.getId() + "-" + "user" + user.getUserId();
        String jsonString = redisMapTools.hGet(TRAILS_MAP_KEY, key, String.class);
        //2 判断是否存在单车
        if(jsonString == null){
            return 0;
        }
        //3 修改单车轨迹数组
        BicycleUsing bicycleUsing = JSONObject.parseObject(jsonString, BicycleUsing.class);
        bicycleUsing.getTrails().add(new Location(bicycle.getJd(),bicycle.getWd()));
        //4 写回redis
        String newJson = JSONObject.toJSONString(bicycleUsing);
        redisMapTools.hput(TRAILS_MAP_KEY,key,newJson);
        return 1;
    }
}
