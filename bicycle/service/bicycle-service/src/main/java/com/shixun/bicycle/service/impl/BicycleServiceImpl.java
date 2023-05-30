package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.BicycleFault;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.shixun.bicycle.mapper.BicycleMapper;
import com.shixun.bicycle.pojo.FixInfo;
import com.shixun.bicycle.service.BicycleFaultService;
import com.shixun.bicycle.service.BicycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shixun.bicycle.utils.DistanceUtil;
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

    @Override
    public List<Bicycle> listBicyclesByIds(List<Integer> ids) {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.in("id",ids);
        return list(bicycleQueryWrapper);
    }

    @Override
    public List<Bicycle> listFaultBicycles(Integer faultId) {
        if(faultId != null){
            BicycleFaultService bicycleFaultService = new BicycleFaultServiceImpl();
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
        return list(null);
    }

    @Override
    public List<Bicycle> listSurroundingBicycles(Double jd, Double wd) {

        QueryWrapper<Bicycle>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",0);
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
        if (bicycle.getState() == 1){
            return 0; // 单车正在使用
        }

        BicycleFaultService bicycleFaultService = new BicycleFaultServiceImpl();

        UpdateWrapper<Bicycle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",bicycleId);
        updateWrapper.ne("state",1);
        updateWrapper.set("state",2);
        update(null,updateWrapper);

        bicycleFaultService.save(new BicycleFault(bicycleId,faultId,new Date()));
        return 1;
    }

    @Override
    public Integer fixBicycleFault(FixInfo fixInfo) {

        BicycleFaultService bicycleFaultService = new BicycleFaultServiceImpl();
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
        updateWrapper.set("state",3);
        update(null,updateWrapper);
        return 1;
    }

    @Override
    public Integer openLock(Integer bicycleId) {
        return null;
    }

    @Override
    public Integer Lock(Integer bicycleId) {
        return null;
    }

    @Override
    public Map<Bicycle, List<Double>> getTrails() {
        return null;
    }

    @Override
    public void addRunnableBicycles(List<Integer> list) {
        UpdateWrapper<Bicycle> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("id",list);
        updateWrapper.eq("state",3);
        updateWrapper.set("state",0);
        update(null,updateWrapper);
    }

    @Override
    public List<Bicycle> getFixedBicycles() {
        QueryWrapper<Bicycle> bicycleQueryWrapper = new QueryWrapper<>();
        bicycleQueryWrapper.eq("state",3);
        return list(bicycleQueryWrapper);
    }
}
