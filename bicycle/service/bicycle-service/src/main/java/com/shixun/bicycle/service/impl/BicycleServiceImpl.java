package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.*;
import cn.itcast.feign.pojo.pack.BicycleUsing;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shixun.bicycle.mapper.BicycleMapper;
import com.shixun.bicycle.pojo.BicycleWithFault;
import com.shixun.bicycle.pojo.FixInfo;
import com.shixun.bicycle.service.BicycleFaultService;
import com.shixun.bicycle.service.BicycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.bicycle.service.FaultService;
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
    public List<BicycleWithFault> listFaultBicycles(Integer faultId) {
        if(faultId != null){
            BicycleFaultService bicycleFaultService = ApplicationContextProvider.getBean(BicycleFaultService.class);
            List<BicycleFault> bicycleFaults = bicycleFaultService.listByFaultId(faultId);
            if(bicycleFaults != null){
                List<Integer> ids = new ArrayList<>();
                for (BicycleFault bicycleFault : bicycleFaults) {
                    ids.add(bicycleFault.getBicycleId());
                }
                FaultService faultService = ApplicationContextProvider.getBean(FaultService.class);
                Fault fault = faultService.getById(faultId);
                QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
                bicycleQueryWrapper.in("id",ids);
                List<Bicycle> bicycleList = list(bicycleQueryWrapper);
                List<BicycleWithFault> bicycleWithFaultList = new ArrayList<>();
                for (Bicycle bicycle : bicycleList) {
                    ArrayList<Fault> faults = new ArrayList<>();
                    faults.add(fault);
                    bicycleWithFaultList.add(new BicycleWithFault(bicycle,faults));
                }
                return bicycleWithFaultList;
            }
            return new ArrayList<>();
        }
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("state",Bicycle.FAULT);
        List<Bicycle> bicycleList = list(bicycleQueryWrapper);
        if (bicycleList == null){
            return new ArrayList<>();
        }
        return generateBicycleWithFault(bicycleList);
    }

    public List<BicycleWithFault> generateBicycleWithFault(List<Bicycle> bicycleList){
        BicycleFaultService bicycleFaultService = ApplicationContextProvider.getBean(BicycleFaultService.class);
        FaultService faultService = ApplicationContextProvider.getBean(FaultService.class);
        List<BicycleWithFault> bicycleWithFaultList = new ArrayList<>();
        for (Bicycle bicycle : bicycleList) {

            Integer bicycleId = bicycle.getId();

            BicycleWithFault bicycleWithFault = new BicycleWithFault();
            bicycleWithFault.setBicycle(bicycle);

            //查该单车的所有fault
            List<BicycleFault> bicycleFaults = bicycleFaultService.listByBicycleId(bicycleId);
            for (BicycleFault bicycleFault : bicycleFaults) {
                Fault fault = faultService.getById(bicycleFault.getFaultId());
                bicycleWithFault.addFault(fault);
            }

            bicycleWithFaultList.add(bicycleWithFault);
        }
        return bicycleWithFaultList;
    }

    @Override
    public List<Bicycle> listSurroundingBicycles(Double jd, Double wd) {

        QueryWrapper<Bicycle>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",Bicycle.FREE);
        List<Bicycle> list = list(queryWrapper);

        List<Bicycle> result = new ArrayList<>();

        for (Bicycle bicycle : list) {
            double distance = DistanceUtil.getDistance(jd,wd,bicycle.getJd(),bicycle.getWd());
            if (distance <= 1000.0){
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

        // 不能重复添加相同故障
        QueryWrapper<BicycleFault> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("bicycle_id",bicycleId);
        queryWrapper.eq("fault_id",faultId);
        if(bicycleFaultService.getOne(queryWrapper) != null){
            return 0;
        };


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
        if(!bicycleHaveFaults.isEmpty()){
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
        bicycleUsing.getBicycle().setMileage(bicycle.getMileage()+bicycleUsing.getBicycle().getMileage());
        bicycleUsing.getBicycle().setJd(bicycle.getJd());
        bicycleUsing.getBicycle().setWd(bicycle.getWd());
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
    public Integer postBicycleTrails(Bicycle bicycle, TUser user) {
        //1 从redis缓存拿到bicycle信息
        String key = "bicycle" + bicycle.getId() + "-" + "user" + user.getUserId();
        String jsonString = redisMapTools.hGet(TRAILS_MAP_KEY, key, String.class);
        //2 判断是否存在单车
        if(jsonString == null){
            return 0;
        }
        //3 修改单车轨迹数组和当前位置，以及里程
        BicycleUsing bicycleUsing = JSONObject.parseObject(jsonString, BicycleUsing.class);
        bicycleUsing.getBicycle().setMileage(bicycle.getMileage()+bicycleUsing.getBicycle().getMileage());
        bicycleUsing.getBicycle().setJd(bicycle.getJd());
        bicycleUsing.getBicycle().setWd(bicycle.getWd());
        bicycleUsing.getTrails().add(new Location(bicycle.getJd(),bicycle.getWd()));
        //4 写回redis
        String newJson = JSONObject.toJSONString(bicycleUsing);
        redisMapTools.hput(TRAILS_MAP_KEY,key,newJson);
        return 1;
    }

    @Override
    public List<Bicycle> listStateBicycles(Integer state) {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("state",state);
        return list(bicycleQueryWrapper);
    }

    @Override
    public List<Bicycle> listHealthyBicycles() {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.in("state",0,1);
        return list(bicycleQueryWrapper);
    }

    @Override
    public Integer addBicyclesLanya(Bicycle bicycle) {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("lanyaid",bicycle.getLanyaid());
        if(getOne(bicycleQueryWrapper) != null){
            return 0;
        }
        save(bicycle);
        return 1;
    }

    @Override
    public Integer getBicycleIdByLanyaId(String lanyaid) {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("lanyaid",lanyaid);
        Bicycle one = getOne(bicycleQueryWrapper);
        if(one != null){
            return one.getId();
        }
        return -1;
    }
}
