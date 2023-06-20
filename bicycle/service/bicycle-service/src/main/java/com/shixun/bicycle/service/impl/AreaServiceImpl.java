package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Area;
import com.shixun.bicycle.mapper.AreaMapper;
import com.shixun.bicycle.service.AreaService;
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
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

}
