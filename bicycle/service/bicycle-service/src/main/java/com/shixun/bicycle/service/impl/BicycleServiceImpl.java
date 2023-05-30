package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Bicycle;
import com.shixun.bicycle.mapper.BicycleMapper;
import com.shixun.bicycle.service.BicycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
