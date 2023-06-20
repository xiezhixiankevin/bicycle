package com.shixun.bicycle.controller;


import cn.itcast.feign.common.R;
import cn.itcast.feign.pojo.Area;
import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.TUser;
import cn.itcast.feign.util.JWTUtils;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.shixun.bicycle.pojo.FixInfo;
import com.shixun.bicycle.service.AreaService;
import com.shixun.bicycle.service.BicycleService;
import com.shixun.bicycle.utils.PredictAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @Autowired
    private AreaService areaService;

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

    /**
     * 根据蓝牙id获取单车id
     * @param
     *
     */
    @PostMapping("/get-bicycle-id-by-lanyaid")
    public R addBicycles(String lanyaid){
        if (lanyaid == null || StringUtils.isEmpty(lanyaid)){
            return R.error().message("请传入蓝牙id");
        }

        return R.ok().data("bicycle_id",bicycleService.getBicycleIdByLanyaId(lanyaid));
    }

    /**
     * 添加单车
     * @param bicycle:其中lanyaid必传
     *
     */
    @PostMapping("/add-bicycles-lanya")
    public R addBicycles(Bicycle bicycle){
        if (bicycle.getLanyaid() == null || StringUtils.isEmpty(bicycle.getLanyaid())){
            return R.error().message("请传入蓝牙id");
        }
        if(bicycleService.addBicyclesLanya(bicycle) == 0){
            return R.error().message("蓝牙id重复");
        }
        return R.ok();
    }

    // 获取某一类故障的所有单车，不传参数获取所有故障单车
    @GetMapping("/list-fault-bicycles")
    public R listFaultBicycles(Integer faultId){
        return R.ok().data("list",bicycleService.listFaultBicycles(faultId));
    }

    // 获取某一类状态的所有单车，0空闲，1使用，2故障，3已修好带投入使用
    @GetMapping("/list-state-bicycles")
    public R listStateBicycles(Integer state){
        return R.ok().data("list",bicycleService.listStateBicycles(state));
    }

    // 获取健康单车
    @GetMapping("/list-healthy-bicycles")
    public R listHealthyBicycles(){
        return R.ok().data("list",bicycleService.listHealthyBicycles());
    }


    // 列出用户附近的所有单车(附近1km内)
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
    public R fixBicycle(@RequestBody String params){
        FixInfo fixInfo = JSONObject.parseObject(params, FixInfo.class);
        Integer result = bicycleService.fixBicycleFault(fixInfo);
        if(result == 1){
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
     * @param params:json格式字符串，自行车id数组
     *
     */
    @PutMapping("/add-running-bicycles")
    public R addRunningBicycles(@RequestBody String params){
        List<Integer> list = JSONObject.parseArray(params,Integer.class);
        bicycleService.addRunnableBicycles(list);
        return R.ok();
    }

    /**
     * 将修好的车投入运行
     * @param idList:自行车id数组
     *
     */
    @PutMapping("/add-running-bicycles-new")
    public R addRunningBicycles(@RequestParam List<Integer> idList){
        bicycleService.addRunnableBicycles(idList);
        return R.ok();
    }

    private TUser getUserInfoFromToken(String token){
        DecodedJWT tokenInfo = JWTUtils.getTokenInfo(token);
        TUser user = new TUser();
        user.setUserId(tokenInfo.getClaim("user_id").asInt());
        user.setIdentify(tokenInfo.getClaim("identify").asInt());
        user.setUserEmail(tokenInfo.getClaim("email").asString());
        user.setUserName(tokenInfo.getClaim("username").asString());
        return user;
    }

    /**
     * 开锁
     * 传单车id
     * */
    @PostMapping("/open-lock")
    public R openLock(Integer bicycleId,HttpServletRequest request){
        String token = request.getHeader("token");
        Integer result = bicycleService.openLock(bicycleId,getUserInfoFromToken(token));
        if(result == 1){
            return R.ok();
        }
        return R.error();
    }

    /**
     * 锁住
     * 传单车id
     * */
    @PostMapping("/lock")
    public R lock(Bicycle bicycle,HttpServletRequest request){
        String token = request.getHeader("token");
        Integer result = bicycleService.Lock(bicycle,getUserInfoFromToken(token));
        if(result == 1){
            return R.ok();
        }
        return R.error();
    }

    // 肖景方 获取最佳停车点
    @GetMapping("/get-best-stop-point/{jd}/{wd}")
    public R getBestStopPoint (@PathVariable("jd") double jd, @PathVariable("wd") double wd) {
        int area = new PredictAlgorithm().readlist(jd, wd);
        if(area == -1){
            return R.error();
        }
        else {
            List<Area> areaList = areaService.listByAreaId(area);
            System.out.println(areaList);
            return R.ok().data("area", areaList.get(0));
        }
    }

    /**
     * 上传单车的轨迹信息
     * 传id,经纬度
     * */
    @PostMapping("/post-bicycle-trails")
    public R postBicycleTrails(Bicycle bicycle,HttpServletRequest request){
        String token = request.getHeader("token");
        if (bicycleService.postBicycleTrails(bicycle,getUserInfoFromToken(token)) == 1){
            return R.ok();
        }
       return R.error().message("当前单车未处于使用状态");
    }

    /**
     * 获取当前所有单车的轨迹信息
     *
     * */
    @PostMapping("/get-bicycle-trails")
    public R getBicycleTrails(){
        return R.ok().data("trail_info",bicycleService.getTrails());
    }


}

