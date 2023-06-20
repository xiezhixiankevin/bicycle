package cn.itcast.feign.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

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
public class BicycleFault implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer bicycleId;

    private Integer faultId;

    @TableField(fill = FieldFill.INSERT)
    private Date createTimestamp;




}
