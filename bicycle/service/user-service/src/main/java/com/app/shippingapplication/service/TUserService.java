package com.app.shippingapplication.service;

import cn.itcast.feign.pojo.TUser;
import cn.itcast.feign.pojo.pack.UserPack;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xzx
 * @since 2023-05-23
 */
public interface TUserService extends IService<TUser> {

    // 注册方法
    TUser registerIntoTable(TUser user);
    UserPack register(UserPack userPack, RedisTemplate<String,String> redisTemplate);
    // 登录方法
    UserPack login(String email, String password);
}
