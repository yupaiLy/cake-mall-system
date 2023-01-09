package cn.twopair.pojo;

import lombok.*;

import java.io.Serializable;

/**
 * @description:
 * @author: 李佳骏
 * @time: 2022/8/25 16:01
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    private String username;
    private String name;
    private String password;
    private String gender;
    private Integer id;
    private String address;
    private String status;
    private static final long serialVersionUID = 2L;
}
