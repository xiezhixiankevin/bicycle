package com.shixun.bicycle.service.impl;

import cn.itcast.feign.pojo.Area;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shixun.bicycle.mapper.AreaMapper;
import com.shixun.bicycle.service.AreaService;
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
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {
    @Override
    public List<Area> listByAreaId(Integer id) {
        QueryWrapper<Area> areaQueryWrapper = new QueryWrapper<>();
        areaQueryWrapper.eq("id", id);
        return list(areaQueryWrapper);
    }
}
