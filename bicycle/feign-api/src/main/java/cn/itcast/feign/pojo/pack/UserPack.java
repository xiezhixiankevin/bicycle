package cn.itcast.feign.pojo.pack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <Description> UserAccountPack
 *
 * @author 26802
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserPack {
    private String userName;

    private String userPassword;

    private String userEmail;

    private Integer identify;

    private String code;

    private String token;
}
