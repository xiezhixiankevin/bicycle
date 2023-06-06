package com.shixun.bicycle.pojo;

import cn.itcast.feign.pojo.Bicycle;
import cn.itcast.feign.pojo.Fault;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;
/**
 * <Description> BicycleWithFault
 *
 * @author 26802
 * @version 1.0
 * @see com.shixun.bicycle.pojo
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BicycleWithFault {

    private Bicycle bicycle;
    private List<Fault> faults = new ArrayList<>();

    public BicycleWithFault addFault(Fault fault){
        faults.add(fault);
        return this;
    }

}
