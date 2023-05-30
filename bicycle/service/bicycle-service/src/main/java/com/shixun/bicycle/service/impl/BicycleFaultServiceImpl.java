package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.BicycleFault;
import com.shixun.bicycle.mapper.BicycleFaultMapper;
import com.shixun.bicycle.service.BicycleFaultService;
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
public class BicycleFaultServiceImpl extends ServiceImpl<BicycleFaultMapper, BicycleFault> implements BicycleFaultService {

}
