package cn.twopair.service.impl;

import cn.twopair.mapper.CakeMapper;
import cn.twopair.mapper.CategoryMapper;
import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Category;
import cn.twopair.result.CatChartPictureOfRosesResult;
import cn.twopair.result.Code;
import cn.twopair.result.Result;
import cn.twopair.service.CategoryService;
import cn.twopair.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Transactional
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CakeMapper cakeMapper;

    @Override
    public List<Category> selectAll() {
        return categoryMapper.selectAll();
    }

    @Override
    public String getInfoByName(String name) {
        Category category = categoryMapper.selectByName(name);
        if (category == null) {
            return JsonTool.createJsonData(Result.error("查询失败！"));
        }
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功"), category, 1));
    }

    @Override
    public String getInfoById(Integer id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            return JsonTool.createJsonData(Result.error("查询失败！"));
        }
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功"), category, 1));

    }

    @Override
    public String getCakeByCatName(HashMap<String, String> params) {
        try {
            String name = params.get("name");
            if (name == null || name.equals("")) {
                return JsonTool.createJsonData(Result.error("分类名参数错误！"));
            }
            List<Cake> cakes = cakeMapper.selectByCategoryName(name);
            if (cakes == null || cakes.size() == 0) {
                return JsonTool.createJsonData(Result.warning("无数据！"));
            }
            int tot = cakes.size();

            Integer currentPage, pageSize;
            try {
                currentPage = Integer.valueOf(params.get("currentPage"));
                pageSize = Integer.valueOf(params.get("pageSize"));
                cakes = cakeMapper.conditionQueryByCategoryName(name, (currentPage - 1) * pageSize, pageSize);
            } catch (Exception e) {
                cakes = cakeMapper.conditionQueryByCategoryName(name, null, null);
            }

            return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功"), cakes, tot));

        } catch (Exception e) {
            return JsonTool.createJsonData(Result.error("查询出错"));

        }
    }

    @Override
    public String getCakeTotalNumInCategory() {
        List<CatChartPictureOfRosesResult> ret = cakeMapper.selectCatNameAndTotNum();
        return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功"), ret, ret.size()));
    }

}
