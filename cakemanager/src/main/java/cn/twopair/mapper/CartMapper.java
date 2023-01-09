package cn.twopair.mapper;

import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author twopair
 * @description 针对表【Cart(购物车)】的数据库操作Mapper
 * @createDate 2022-12-12 15:01:11
 * @Entity cn.twopair.pojo.Cart
 */
public interface CartMapper {

    @Select("SELECT * FROM cart")
    List<Cart> selectAll();

    @Select("SELECT * FROM cart WHERE userId = (SELECT id FROM user WHERE username = #{username})")
    List<Cart> selectByUsername(String username);
    @Select("SELECT * FROM cart WHERE userId = #{userId}")
    List<Cart> selectByUserId(Integer userId);
    @Select("SELECT * FROM cart WHERE userId = #{userId} AND cakeId = #{cakeId}")
    Cart selectByUserIdAndCakeId(Cart cart);


    @Insert("INSERT INTO cart VALUES (null, #{userId}, #{cakeId}, #{num})")
    int insert(Cart cart);

    @Update("UPDATE cart SET num = #{num} " +
            "WHERE userId = #{userId} AND cakeId = #{cakeId}")
    int update(Cart cart);

    @Delete("DELETE FROM cart WHERE userId = #{userId} AND cakeId = #{cakeId}")
    int deleteByUserIdAndCakeId(Cart cart);

    @Delete("DELETE FROM cart WHERE userId = #{userId}")
    int deleteAllByUserId(Integer userId);
}
