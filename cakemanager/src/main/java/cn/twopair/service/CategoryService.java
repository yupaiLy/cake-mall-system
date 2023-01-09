package cn.twopair.service;

import cn.twopair.pojo.Category;

import java.util.HashMap;
import java.util.List;

public interface CategoryService {
    List<Category> selectAll();

    String getInfoByName(String name);

    String getInfoById(Integer id);

    String getCakeByCatName(HashMap<String, String> params);

    String getCakeTotalNumInCategory();
}
