package com.app.shippingapplication.service.impl;

import cn.itcast.feign.pojo.TUser;
import cn.itcast.feign.pojo.pack.UserPack;
import cn.itcast.feign.util.JWTUtils;
import com.app.shippingapplication.mapper.TUserMapper;
import com.app.shippingapplication.service.TUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xzx
 * @since 2023-05-23
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements TUserService {

    @Override
    public TUser registerIntoTable(TUser user) {
        try{
            save(user);
            return user;
        }catch (DuplicateKeyException e){
            // 邮箱唯一
            return null;
        }
    }

    @Override
    public UserPack register(UserPack userPack, RedisTemplate<String, String> redisTemplate) {
        String code = userPack.getCode();
        String email = userPack.getUserEmail();

        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        String realCode = forValue.get(email + "-shixun-register-code");
        if(realCode ==null){
            return null;
        }else if(realCode.equals(code)){
            // 验证码正确
            if(registerIntoTable(new TUser(userPack)) != null){
                userPack.setUserPassword("");
                userPack.setCode("");
                return userPack;
            }
            // 已经注册过账号
            return null;
        }else return null;
    }

    @Override
    public UserPack login(String email, String password) {
        QueryWrapper<TUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_email",email);
        queryWrapper.eq("user_password",password);
        TUser userAccount = getOne(queryWrapper);
        if(userAccount != null){
            // 生成token
            Map<String,String> map = new HashMap<>();
            map.put("email",email);
            map.put("username",userAccount.getUserName());
            map.put("identify",userAccount.getIdentify().toString());
            String token = JWTUtils.getToken(map);

            UserPack userPack = new UserPack();
            userPack.setUserEmail(email);
            userPack.setUserName(userAccount.getUserName());
            userPack.setIdentify(userAccount.getIdentify());
            userPack.setToken(token);
            return userPack;
        }
        return null;
    }
}
