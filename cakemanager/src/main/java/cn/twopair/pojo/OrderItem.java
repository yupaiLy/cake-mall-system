package cn.twopair.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单详情表
 * @TableName orderItem
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 商品id
     */
    private Integer pid;

    /**
     * 订单id
     */
    private Integer oid;

    /**
     * 客户id
     */
    private Integer uid;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 总金额
     */
    private Double total;

    private static final long serialVersionUID = 6L;
}