package com.shixun.bicycle.service;

import cn.itcast.feign.pojo.Bicycle;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun.bicycle.pojo.FixInfo;

import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
public interface BicycleService extends IService<Bicycle> {

    List<Bicycle> listBicyclesByIds(List<Integer> ids);

    List<Bicycle> listFaultBicycles(Integer faultId);

    List<Bicycle> listSurroundingBicycles(Double jd, Double wd);

    Integer addFaultBicycle(Integer bicycleId, Integer faultId);

    Integer fixBicycleFault(FixInfo fixInfo);

    Integer openLock(Integer bicycleId);

    Integer Lock(Integer bicycleId);

    Map<Bicycle,List<Double>> getTrails();

    void addRunnableBicycles(List<Integer> list);

    List<Bicycle> getFixedBicycles();

    void addBicycles(List<Bicycle> bicycles);

    // 增删改查

    //

}
