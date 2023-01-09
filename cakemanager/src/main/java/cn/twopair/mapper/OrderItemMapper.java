package cn.twopair.mapper;

import cn.twopair.pojo.OrderItem;
import org.apache.ibatis.annotations.Insert;

/**
* @author twopair
* @description 针对表【orderItem(订单详情表)】的数据库操作Mapper
* @createDate 2022-12-15 13:07:40
* @Entity cn.twopair.pojo.Orderitem
*/
public interface OrderItemMapper {

    int deleteByPrimaryKey(Long id);

    @Insert("INSERT INTO orderItem VALUES(null, #{pid}, #{oid}, #{uid}, #{number}, #{total})")
    int insert(OrderItem orderItem);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);

}
