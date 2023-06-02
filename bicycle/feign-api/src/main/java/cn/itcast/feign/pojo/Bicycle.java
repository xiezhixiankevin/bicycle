package cn.itcast.feign.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xzx
 * @since 2023-05-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Bicycle implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer FREE = 0;
    public static final Integer USING = 1;
    public static final Integer FAULT = 2;
    public static final Integer RUNNABLE = 3;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Double jd = 39.952536;

    private Double wd = 116.34348;

    /**
     * 0空闲，1使用，2故障,3带投入使用
     */
    private Integer state = 0;

    /**
     * 停车区域id
     */
    private Integer area = 0;


}
