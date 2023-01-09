package cn.twopair.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单表
 *
 * @TableName order_
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    /**
     *
     */
    private Integer id;


    /**
     * 订单编号
     */
    private String code;

    /**
     * 买家id
     */
    private String userId;

    /**
     * 订单状态0：未支付，1：已支付 ， 2：未发货，3：已发货， 4：已收货， 5：已完结，6：已取消
     */
    private String status;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 收货人
     */
    private String name;

    /**
     * 收货电话
     */
    private String phone;

    /**
     * 总金额
     */
    private Double total;

    /**
     * 下单时间
     */
    private String orderTime;

    private static final long serialVersionUID = 5L;
}