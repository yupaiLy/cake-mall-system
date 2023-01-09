package cn.twopair.pojo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 分类表
 *
 * @TableName category
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    private Integer fatherId;

    private Integer type;

    private static final long serialVersionUID = 1L;
}