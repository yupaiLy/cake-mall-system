package cn.twopair.mapper;

import cn.twopair.pojo.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author twopair
 * @description 针对表【category(分类表)】的数据库操作Mapper
 * @createDate 2022-12-05 16:26:53
 * @Entity cn.twopair.pojo.Category
 */
public interface CategoryMapper {

    @Select("SELECT * FROM category ")
    List<Category> selectAll();

    @Select("SELECT * FROM category WHERE id=#{id}")
    Category selectById(@Param("id") Integer id);

    @Insert("INSERT INTO category VALUES (null, #{name})")
    int insert(Category category);

    @Delete("DELETE FROM category WHERE id=#{id}")
    int deleteById(Integer id);

    int deleteByIds(@Param("ids") List<Integer> ids);

    @Update("UPDATE category SET name=#{name} WHERE id=#{id}")
    int update(Category category);

    @Select("SELECT * FROM category WHERE name=#{name}")
    Category selectByName(String name);

}
