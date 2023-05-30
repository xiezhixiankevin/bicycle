package com.shixun.bicycle.controller;


import cn.itcast.feign.common.R;
import com.shixun.bicycle.service.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/list-by-fault-id")
    public R listByFaultId(){
        return R.ok();
    }

    @GetMapping("/list-by-area-id")
    public R listByAreaId(){
        return R.ok();
    }

    @PostMapping("/add-fault-bicycle")
    public R addFaultBicycle(){
        return R.ok();
    }

    @PutMapping("/fix-bicycle")
    public R fixBicycle(){
        return R.ok();
    }

    @PostMapping("/open-lock")
    public R openLock(){
        return R.ok();
    }

    @PostMapping("/lock")
    public R lock(){
        return R.ok();
    }

    //肖景方
    @GetMapping("/get-best-stop-point")
    public R getBestStopPoint(){
        return R.ok();
    }



}

