package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.BicycleFault;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.bicycle.mapper.BicycleFaultMapper;
import com.shixun.bicycle.service.BicycleFaultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
@Service
public class BicycleFaultServiceImpl extends ServiceImpl<BicycleFaultMapper, BicycleFault> implements BicycleFaultService {

    @Override
    public List<BicycleFault> listByFaultId(Integer id) {
        QueryWrapper<BicycleFault> bicycleFaultWrapper = new QueryWrapper<>();
        bicycleFaultWrapper.eq("fault_id",id);
        return list(bicycleFaultWrapper);
    }

    @Override
    public List<BicycleFault> listByBicycleId(Integer bicycleId) {
        QueryWrapper<BicycleFault> bicycleFaultWrapper = new QueryWrapper<>();
        bicycleFaultWrapper.eq("bicycle_id",bicycleId);
        return list(bicycleFaultWrapper);
    }
}
