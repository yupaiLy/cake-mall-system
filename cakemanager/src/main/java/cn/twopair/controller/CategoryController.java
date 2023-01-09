package cn.twopair.controller;

import cn.twopair.pojo.Category;
import cn.twopair.result.Code;
import cn.twopair.result.Result;
import cn.twopair.service.CategoryService;
import cn.twopair.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/categorys")
@ResponseBody
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping(produces = "application/json;charset=utf-8")
    public String getAllCategory() {
        List<Category> categories = categoryService.selectAll();

        Result<List<Category>> result = new Result<>();
        result.setData(categories);
        result.setCode(Code.createCode(Code.SUCCESS, "查询成功!"));
        result.setData_num(categories.size());

        String ret = JsonTool.createJsonData(result);
        System.out.println(ret);
        return ret;

    }

    @GetMapping(value = "/getInfoById", produces = "application/json;charset=utf-8")
    public String getInfoById(@RequestParam Integer id) {
        return categoryService.getInfoById(id);
    }

    @GetMapping(value = "/getInfoByName", produces = "application/json;charset=utf-8")
    public String getInfoByName(@RequestParam String name) {
        return categoryService.getInfoByName(name);
    }

    @PostMapping(value = "/getCakeByCatName", produces = "application/json;charset=utf-8")
    public String getCakeByCame(@RequestBody HashMap<String, String> params) {
        return categoryService.getCakeByCatName(params);
    }

    /**
     * 获取每个分类下产品的总销售量
     * @return
     */
    @GetMapping(value = "/getCakeTotalNumInCategory", produces = "application/json;charset=utf-8")
    public String getCakeTotalNumInCategory(){
        return categoryService.getCakeTotalNumInCategory();
    }
}
