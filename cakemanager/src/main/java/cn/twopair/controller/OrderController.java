package cn.twopair.controller;

import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Order;
import cn.twopair.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@ResponseBody
public class OrderController {

    @Autowired
    private OrderService orderService;
    @PostMapping(produces = "application/json;charset=utf-8")
    public  String addOrder(@RequestBody HashMap<String, String> params) {
        return orderService.addOrder(params);
    }
    @GetMapping(value = "/{username}",produces = "application/json;charset=utf-8")
    public String showOrder(@PathVariable("username") String username){
        return orderService.showOrder(username);
    }

    @PutMapping(value = "/updateStatus",produces = "application/json;charset=utf-8")
    public String updateStatus(@RequestBody HashMap<String, String> params){
        return orderService.updateStatusByCode(params);
    }

    @PostMapping(value = "/choose", produces = "application/json;charset=utf-8")
    public String getData(@RequestBody HashMap<String, String> params) {
        System.out.println(params);
        int currentPage = Integer.parseInt(params.get("currentPage"));
        int pageSize = Integer.parseInt(params.get("pageSize"));
        Order order = new Order();
        order.setCode(params.get("code"));
        order.setPhone(params.get("phone"));
        order.setStatus(params.get("status"));

        return orderService.conditionQuery(order, currentPage, pageSize);
    }

    @PutMapping(produces = "application/json;charset=utf-8")
    public String updateOrderByBackground(@RequestBody Order order){
        return orderService.updateByBackground(order);
    }


    @DeleteMapping(produces = "application/json;charset=utf-8")
    public String deleteOrder(@RequestBody HashMap<String, List<Integer>> params) {
        List<Integer> ids = params.get("idList");
        return orderService.deleteByIds(ids);
    }

}
