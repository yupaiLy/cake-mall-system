package cn.twopair.mapper;

import cn.twopair.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author twopair
 * @description 针对表【order_(订单表)】的数据库操作Mapper
 * @createDate 2022-12-15 13:07:18
 * @Entity cn.twopair.pojo.Order
 */
public interface OrderMapper {

    @Insert("Insert INTO order_ VALUES (null, #{code}, #{userId}, #{status}," +
            " #{address}, #{name}, #{phone}, #{total}, #{orderTime})")
    int insert(Order order);

    @Select("SELECT * FROM order_ WHERE code = #{code}")
    Order selectByCode(String code);


    @Select("SELECT * FROM order_ WHERE userId = (SELECT id FROM user WHERE username = #{username})")
    List<Order> selectByUsername(String username);

    @Update("UPDATE order_ SET status = #{status} WHERE code = #{code}")
    int updateStatusByCode(@Param("code") String code, @Param("status") String status);


    List<Order> conditionQuery(@Param("order") Order order, @Param("index") Integer index, @Param("size") Integer size);

    @Update("UPDATE order_ SET status = #{status}, address = #{address}, name = #{name}," +
            " phone = #{phone}, total = #{total} WHERE code = #{code}")
    int update(Order order);

    int deleteByIds(@Param("ids") List<Integer> ids);
}
