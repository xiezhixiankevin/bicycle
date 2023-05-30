package com.shixun.bicycle.controller;


import cn.itcast.feign.common.R;
import com.shixun.bicycle.service.AreaService;
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
@RequestMapping("/area")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/list-areas")
    public R listAreas(){
        return R.ok();
    }

    @PostMapping("/add-area")
    public R addArea(){
        return R.ok();
    }

    @PutMapping("/update-area")
    public R updateArea(){
        return R.ok();
    }

    @PostMapping("/get-best-stop-area")
    public R getBestStopArea(){
        return R.ok();
    }
}

