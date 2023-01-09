package cn.twopair.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车
 * @TableName Cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 商品id
     */
    private Integer cakeId;

    /**
     * 数量
     */
    private Integer num;

    private static final long serialVersionUID = 4L;
}