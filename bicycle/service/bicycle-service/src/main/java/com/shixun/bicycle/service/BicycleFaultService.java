package com.shixun.bicycle.service;

import cn.itcast.feign.pojo.BicycleFault;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.*;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
public interface BicycleFaultService extends IService<BicycleFault> {

    // 根据某个faultId获取条目
    List<BicycleFault> listByFaultId(Integer id);

}
