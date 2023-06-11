package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Fault;
import com.shixun.bicycle.mapper.FaultMapper;
import com.shixun.bicycle.service.FaultService;
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
public class FaultServiceImpl extends ServiceImpl<FaultMapper, Fault> implements FaultService {

}
