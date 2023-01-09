package cn.twopair.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品表
 *
 * @TableName product
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cake implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品价格
     */
    private Double price;

    /**
     * 商品销量
     */
    private Integer number;

    /**
     * 状态0：下架， 1：上架
     */
    private String status;

    /**
     * 商品图片url
     */
    private String imgUrl;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类id
     */
    private String cid;

    /**
     * 赞数量
     */
    private Integer likesNumber;

    private static final long serialVersionUID = 4L;
}