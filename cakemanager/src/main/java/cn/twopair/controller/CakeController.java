package cn.twopair.controller;

import cn.twopair.pojo.Cake;
import cn.twopair.service.CakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/cakes")
@ResponseBody
public class CakeController {
    @Autowired
    private CakeService cakeService;

    @PostMapping(produces = "application/json;charset=utf-8")
    public String addCake(@RequestBody Cake Cake) {
        return cakeService.add(Cake);
    }

    @DeleteMapping(produces = "application/json;charset=utf-8")
    public String deleteCake(@RequestBody HashMap<String, List<Integer>> params) {
        List<Integer> ids = params.get("idList");
        return cakeService.deleteByIds(ids);
    }

    @PutMapping(produces = "application/json;charset=utf-8")
    public String modifyCake(@RequestBody Cake Cake) {
        return cakeService.updateCake(Cake);
    }


    @PostMapping(value = "/choose", produces = "application/json;charset=utf-8")
    public String getData(@RequestBody HashMap<String, String> params) {
        System.out.println(params);
        int currentPage = Integer.parseInt(params.get("currentPage"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        Cake Cake = new Cake();
        Cake.setName(params.get("name"));
        Cake.setStatus(params.get("status"));
        if (params.get("cid") != null) Cake.setCid(params.get("cid"));
        System.out.println(pageSize);
        return cakeService.conditionQuery(Cake, currentPage, pageSize);
    }

    /**
     * 获取每个产品的销售量
     * @return
     */
    @GetMapping(value = "/getAllCakeNum", produces = "application/json;charset=utf-8")
    public String getCakeTotalNumInCategory(){
        return cakeService.getAllCakeNum();
    }

    @GetMapping(value = "/getHotCake", produces = "application/json;charset=utf-8")
    public String getHotCake(){
        return cakeService.getHotCake();
    }
}
