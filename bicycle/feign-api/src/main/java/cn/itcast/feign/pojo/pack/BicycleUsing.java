package cn.itcast.feign.pojo.pack;

import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

/**
 * <Description> BicycleUsing
 *
 * @author 26802
 * @version 1.0
 * @see cn.itcast.feign.pojo.pack
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BicycleUsing {

    private Bicycle bicycle;

    private Integer userId;

    private String userName;

    private String userEmail;

    private List<Location> trails = new ArrayList<>();

}
