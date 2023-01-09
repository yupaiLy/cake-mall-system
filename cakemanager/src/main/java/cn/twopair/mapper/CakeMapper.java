package cn.twopair.mapper;

import cn.twopair.pojo.Cake;
import cn.twopair.result.CatChartPictureOfRosesResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author twopair
 * @description 针对表【product(商品表)】的数据库操作Mapper
 * @createDate 2022-12-09 15:14:08
 * @Entity cn.twopair.pojo.Product
 */
public interface CakeMapper {

    @Select("SELECT * FROM product WHERE name = #{name}")
    Cake selectByName(String name);


    @Insert("INSERT INTO product VALUES (NULL, #{name}, #{price}, #{number}, " +
            "#{status}, #{imgUrl}, #{description}, #{cid}, #{likesNumber})")
    int insert(Cake cake);

    int deleteByIds(@Param("ids") List<Integer> ids);

    @Update("UPDATE product SET name=#{name}, price=#{price}, number=#{number}," +
            "status=#{status}, imgUrl = #{imgUrl}, description = #{description}, cid = #{cid}, " +
            "likesNumber = #{likesNumber} WHERE id = #{id}")
    int update(Cake cake);


    List<Cake> conditionQuery(@Param("cake") Cake cake, @Param("index") int index, @Param("size") int size);

    List<Cake> conditionQueryData(@Param("cake") Cake cake);

    @Select("SELECT * FROM product WHERE id = #{id}")
    Cake selectById(Integer id);

    @Select("SELECT * FROM product WHERE cid = (SELECT id FROM category WHERE name = #{name})")
    List<Cake> selectByCategoryName(String name);

    List<Cake> conditionQueryByCategoryName(@Param("name") String name, @Param("index") Integer index, @Param("size") Integer pageSize);

    @Select("SELECT category.name as name, sum(number) AS value FROM product, category " +
            "WHERE category.id = product.cid GROUP BY product.cid")
    List<CatChartPictureOfRosesResult> selectCatNameAndTotNum();


    @Select("SELECT * FROM product")
    List<Cake> selectAll();

    @Select("SELECT * FROM product ORDER BY number DESC LIMIT #{index}, #{size}")
    List<Cake> selectOrderByNum(@Param("index") int index, @Param("size") int size);
}
