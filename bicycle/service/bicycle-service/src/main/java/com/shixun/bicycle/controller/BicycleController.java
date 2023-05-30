package com.shixun.bicycle.controller;


import cn.itcast.feign.common.R;
import cn.itcast.feign.pojo.Bicycle;
import com.alibaba.fastjson.JSONObject;
import com.shixun.bicycle.pojo.FixInfo;
import com.shixun.bicycle.service.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
@RestController
@RequestMapping("/bicycle")
public class BicycleController {
    @Autowired
    private BicycleService bicycleService;

    /**
     * 添加单车
     * @param num:要添加的单车数量
     *
     */
    @PostMapping("/add-bicycles")
    public R addBicycles(Integer num){
        List<Bicycle> bicycles = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            bicycles.add(new Bicycle());
        }
        bicycleService.addBicycles(bicycles);
        return R.ok();
    }

    // 获取某一类故障的所有单车，不传参数获取所有故障单车
    @GetMapping("/list-fault-bicycles")
    public R listFaultBicycles(Integer faultId){
        return R.ok().data("list",bicycleService.listFaultBicycles(faultId));
    }

    // 列出用户附近的所有单车(附近500m内)
    @GetMapping("/list-surrounding-bicycles")
    public R listSurroundingBicycles(Double jd,Double wd){
        return R.ok().data("list",bicycleService.listSurroundingBicycles(jd,wd));
    }

    // 给单车添加一个故障信息
    @PostMapping("/add-fault-bicycle")
    public R addFaultBicycle(Integer bicycleId,Integer faultId){
        Integer result = bicycleService.addFaultBicycle(bicycleId,faultId);
        if(result == 1){
            return R.ok();
        }
        return R.error();

    }

    /**
     * 修复单车的一个或多个故障
     * @param params:json格式字符串,格式参照FixInfo
     *
     */
    @PutMapping("/fix-bicycle-fault")
    public R fixBicycle(String params){
        FixInfo fixInfo = JSONObject.parseObject(params, FixInfo.class);
        Integer result = bicycleService.fixBicycleFault(fixInfo);
        if(result == 0){
            return R.ok();
        }
        return R.error();
    }

    // 获取已经修好的车
    @GetMapping("/get-fixed-bicycles")
    public R getFixedBicycles(){
        return R.ok().data("list",bicycleService.getFixedBicycles());
    }

    /**
     * 将修好的车投入运行
     * @param params:json格式字符串，数组
     *
     */
    @PutMapping("/add-running-bicycles")
    public R addRunningBicycles(String params){
        List<Integer> list = JSONObject.parseArray(params,Integer.class);
        bicycleService.addRunnableBicycles(list);
        return R.ok();
    }

    // 开锁
    @PostMapping("/open-lock")
    public R openLock(Integer bicycleId){
        Integer result = bicycleService.openLock(bicycleId);
        if(result == 0){
            return R.ok();
        }
        return R.error();
    }

    // 锁住
    @PostMapping("/lock")
    public R lock(Integer bicycleId){
        Integer result = bicycleService.Lock(bicycleId);
        if(result == 0){
            return R.ok();
        }
        return R.error();
    }

    // 肖景方 获取最佳停车点
    @GetMapping("/get-best-stop-point")
    public R getBestStopPoint(){
        return R.ok();
    }

    // 获取当前所有单车的轨迹信息
    @GetMapping("/get-bicycle-trails")
    public R getBicycleTrails(){
        return R.ok().data("trail_info",bicycleService.getTrails());
    }


}

