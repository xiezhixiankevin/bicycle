package cn.itcast.feign.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <Description> Location
 *
 * @author 26802
 * @version 1.0
 * @see cn.itcast.feign.pojo
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Location {

    private Double jd;
    private Double wd;

}
