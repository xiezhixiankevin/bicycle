package com.shixun.bicycle.service;

import cn.itcast.feign.pojo.Area;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
public interface AreaService extends IService<Area> {
    List<Area> listByAreaId(Integer id);

}
