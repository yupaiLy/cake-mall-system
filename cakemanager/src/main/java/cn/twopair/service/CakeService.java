package cn.twopair.service;

import cn.twopair.pojo.Cake;

import java.util.List;

public interface CakeService {
    String add(Cake cake);

    String deleteByIds(List<Integer> ids);

    String updateCake(Cake cake);

    String conditionQuery(Cake cake, int currentPage, int pageSize);

    String getAllCakeNum();

    String getHotCake();

}
