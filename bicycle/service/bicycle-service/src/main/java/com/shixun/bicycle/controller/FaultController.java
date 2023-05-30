package com.shixun.bicycle.controller;


import cn.itcast.feign.common.R;
import cn.itcast.feign.pojo.Fault;
import com.shixun.bicycle.service.BicycleFaultService;
import com.shixun.bicycle.service.FaultService;
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
@RequestMapping("/fault")
public class FaultController {

    @Autowired
    private FaultService faultService;

    @Autowired
    private BicycleFaultService bicycleFaultService;

    @PostMapping("/add-fault")
    public R addFault(Fault fault){
        faultService.save(fault);
        return R.ok();
    }

    @GetMapping("/list-fault")
    public R listFault(){
        return R.ok().data("list",faultService.list(null));
    }

    @DeleteMapping("/delete-fault")
    public R deleteFault(Fault fault){
        if(bicycleFaultService.listByFaultId(fault.getId()) == null){
            faultService.removeById(fault.getId());
            return R.ok();
        }
        return R.error().message("该故障存在未处理单车");

    }

    @PutMapping("/update-fault")
    public R updateFault(Fault fault){
        faultService.updateById(fault);
        return R.ok();
    }

}

