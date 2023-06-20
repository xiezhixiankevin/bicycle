package com.shixun.bicycle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

/**
 * <Description> FixInfo
 *
 * @author 26802
 * @version 1.0
 * @see com.shixun.bicycle.pojo
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FixInfo {

    private Integer bicycleId;
    private List<Integer> faultIds;

}
