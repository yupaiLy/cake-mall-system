package cn.twopair.service.impl;

import cn.twopair.mapper.*;
import cn.twopair.pojo.*;
import cn.twopair.result.Code;
import cn.twopair.result.OrderViewResult;
import cn.twopair.result.Result;
import cn.twopair.service.OrderService;
import cn.twopair.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private CakeMapper cakeMapper;

    @Override
    public String addOrder(HashMap<String, String> params) {
        try {
            Integer userId = userMapper.selectByUsername(params.get("username")).getId();
            Integer status = Integer.valueOf(params.get("status"));
            String address = params.get("address");
            String name = params.get("name");
            String phone = params.get("phone");
            Double total = Double.valueOf(params.get("total"));
            Order orderParam = new Order(null, String.valueOf(System.currentTimeMillis()),
                    String.valueOf(userId), String.valueOf(status), address, name, phone, total, String.valueOf(System.currentTimeMillis()));

            if (orderMapper.insert(orderParam) > 0) {
                List<Cart> carts = cartMapper.selectByUserId(userId);
                assert (carts.size() != 0);
                Integer oid = orderMapper.selectByCode(orderParam.getCode()).getId();
                for (Cart cart : carts) {
                    Double tot = cakeMapper.selectById(cart.getCakeId()).getPrice() * cart.getNum();
                    Integer num = cart.getNum();
                    Integer uid = cart.getUserId();
                    Integer pid = cart.getCakeId();
                    orderItemMapper.insert(new OrderItem(null, pid, oid, uid, num, tot));
                }
                return JsonTool.createJsonData(Result.success("下单成功！"));
            }
            return JsonTool.createJsonData(Result.error("下单失败！"));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonTool.createJsonData(Result.error("下单失败！"));
        }
    }

    @Override
    public String showOrder(String username) {

        try {
            List<Order> orders = orderMapper.selectByUsername(username);
            converse(orders);

            List<OrderViewResult> ret = new ArrayList<>();
            for (Order order : orders) {
                OrderViewResult t = new OrderViewResult();
                t.setCode(order.getCode());
                t.setName(order.getName());
                t.setAddress(order.getAddress());
                t.setPhone(order.getPhone());
                t.setTotal(order.getTotal());
                t.setStatus(order.getStatus());
                t.setOrderTime(order.getOrderTime());
                ret.add(t);
            }
            return JsonTool.createJsonData(Result.warning(Code.createCode(Code.SUCCESS, "查询成功!"), ret, ret.size()));

        } catch (Exception e) {
            return JsonTool.createJsonData(Result.error("查询订单失败！"));
        }
    }

    @Override
    public String updateStatusByCode(HashMap<String, String> params) {
        try {
            String code = params.get("code");
            String status = params.get("status");

            int ok = orderMapper.updateStatusByCode(code, status);
            if (ok > 0) {
                return JsonTool.createJsonData(Result.success("修改订单状态成功！"));
            }
            return JsonTool.createJsonData(Result.error("修改订单状态失败！"));
        } catch (Exception e) {
            return JsonTool.createJsonData(Result.error("修改订单状态失败！"));
        }
    }

    @Override
    public String conditionQuery(Order order, Integer currentPage, Integer pageSize) {
        List<Order> orders = orderMapper.conditionQuery(order, (currentPage - 1) * pageSize, pageSize);
        // 数据库总条数
        int tot = orderMapper.conditionQuery(order, null, null).size();

        // 防止点击最后一夜再条件查询出现空情况
        if (tot < (currentPage - 1) * pageSize) {
            orders = orderMapper.conditionQuery(order, 0, pageSize);
            // 状态转换
            converse(orders);
            return JsonTool.createJsonData(Result.warning(Code.createCode(Code.WARNING, "查询成功!"), orders, tot));
        }
        converse(orders);
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), orders, tot));
    }

    @Override
    public String updateByBackground(Order order) {
        try {
            Integer userId = userMapper.selectByUsername(order.getUserId()).getId();
            order.setUserId(String.valueOf(userId));
            int ok = orderMapper.update(order);
            if (ok > 0) {
                return JsonTool.createJsonData(Result.success("修改订单成功！"));
            }
            return JsonTool.createJsonData(Result.error("修改订单失败！"));
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonTool.createJsonData(Result.error("修改订单失败！"));
        }
    }

    @Override
    public String deleteByIds(List<Integer> ids) {
        int cnt = orderMapper.deleteByIds(ids);
        if (cnt == ids.size()) {
            return JsonTool.createJsonData(Result.success("删除成功!"));
        }
        //回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return JsonTool.createJsonData(Result.error("删除失败！请稍后再试"));
    }

    private void converse(List<Order> orders) {
        if (orders.size() == 0) return;
        for (Order order : orders) {
            switch (order.getStatus()) {
                case "0" -> order.setStatus("未支付");
                case "1" -> order.setStatus("已支付");
                case "2" -> order.setStatus("未发货");
                case "3" -> order.setStatus("已发货");
                case "4" -> order.setStatus("已收货");
                case "5" -> order.setStatus("已完结");
                case "6" -> order.setStatus("已取消");
            }
            order.setUserId(userMapper.selectById(order.getUserId()).getUsername());
            String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(order.getOrderTime())));
            order.setOrderTime(orderTime);
        }
    }
}
