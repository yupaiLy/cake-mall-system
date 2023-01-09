package cn.twopair.mapper;

import cn.twopair.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description:
 * @author: 李佳骏
 * @time: 2022/8/25 16:15
 */

public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username}")
    User selectByUsername(String username);

    /**
     * gh
     *
     * @Param:
     * @Description: 查询所有用户信息
     * @Return: java.util.List<cn.twopair.pojo.User>
     * @Author: 李佳骏
     * @Date: 2022/12/5 10:19
     */
    @Select("SELECT * FROM user")
    List<User> selectAll();

    List<User> conditionQuery(@Param("user") User user, @Param("index") int index, @Param("size") int size);

    List<User> conditionQueryData(@Param("user") User user);

    /**
     * @Param: index
     * @Param: size
     * @Description: 已弃用
     * @Return: java.util.List<cn.twopair.pojo.User>
     * @Author: 李佳骏
     * @Date: 2022/12/6 22:08
     */
    @Select("SELECT * FROM user LIMIT #{index}, #{size}")
    List<User> selectByPage(@Param("index") int index, @Param("size") int size);

    /**
     * @Param: user 需插入的user对象
     * @Description: 插入user信息
     * @Return: void
     * @Author: 李佳骏
     * @Date: 2022/9/20 19:25
     */
    @Insert("INSERT INTO user VALUES (NULL, #{username}, #{password}, " +
            "#{name}, #{gender}, #{address}, #{status})")
    int insert(User user);

    @Update("UPDATE user SET username = #{username}, password = #{password}, name = #{name}," +
            " gender = #{gender}, address = #{address}, status = #{status} WHERE id = #{id}")
    int update(User user);

    @Delete("DELETE  FROM user WHERE id = #{id}")
    int delete(@Param("id") Integer id);

    int deleteByIds(@Param("ids") List<Integer> ids);

    @Select("SELECT * FROM user WHERE id = #{userId}")
    User selectById(String userId);

}
