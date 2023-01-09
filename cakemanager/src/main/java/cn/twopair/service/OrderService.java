package cn.twopair.service;

import cn.twopair.pojo.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderService {
    String addOrder(HashMap<String, String> params);

    String showOrder(String username);

    String updateStatusByCode(HashMap<String, String> params);

    String conditionQuery(Order order, Integer currentPage, Integer pageSize);

    String updateByBackground(Order order);

    String deleteByIds(List<Integer> ids);
}
