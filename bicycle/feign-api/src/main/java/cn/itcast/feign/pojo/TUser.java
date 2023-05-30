package cn.itcast.feign.pojo;

import cn.itcast.feign.pojo.pack.UserPack;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xzx
 * @since 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    private String userName;

    private String userPassword;

    private String userEmail;

    /**
     * 身份，0普通用户，1普通管理员，2最高管理员
     */
    private Integer identify;

    @TableLogic
    private Integer status;

    public TUser(UserPack userPack) {
        setUserName(userPack.getUserName());
        setUserPassword(userPack.getUserPassword());
        setUserEmail(userPack.getUserEmail());
        setIdentify(userPack.getIdentify());
    }

}
