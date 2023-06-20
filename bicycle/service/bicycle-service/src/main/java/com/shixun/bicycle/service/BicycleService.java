package com.shixun.bicycle.service;

import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.TUser;
import cn.itcast.feign.pojo.pack.BicycleUsing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.shixun.bicycle.pojo.BicycleWithFault;
import com.shixun.bicycle.pojo.FixInfo;
import io.swagger.models.auth.In;

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

    List<BicycleWithFault> listFaultBicycles(Integer faultId);

    List<Bicycle> listSurroundingBicycles(Double jd, Double wd);

    Integer addFaultBicycle(Integer bicycleId, Integer faultId);

    Integer fixBicycleFault(FixInfo fixInfo);

    Integer openLock(Integer bicycleId, TUser user);

    Integer Lock(Bicycle bicycle,TUser user);

    List<BicycleUsing> getTrails();

    void addRunnableBicycles(List<Integer> list);

    List<Bicycle> getFixedBicycles();

    void addBicycles(List<Bicycle> bicycles);

    Integer postBicycleTrails(Bicycle bicycle, TUser user);

    List<Bicycle> listStateBicycles(Integer state);

    List<Bicycle> listHealthyBicycles();

    Integer addBicyclesLanya(Bicycle bicycle);

    Integer getBicycleIdByLanyaId(String lanyaid);

    // 增删改查

    //

}
