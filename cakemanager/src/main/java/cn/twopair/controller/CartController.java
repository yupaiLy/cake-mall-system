package cn.twopair.controller;

import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Cart;
import cn.twopair.pojo.User;
import cn.twopair.result.Result;
import cn.twopair.service.CakeService;
import cn.twopair.service.CartService;
import cn.twopair.service.UserService;
import cn.twopair.util.JsonTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/carts")
@ResponseBody
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 详情页面增加调用接口
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/detailsCart", produces = "application/json;charset=utf-8")
    public String addCartDetailsCart(@RequestBody HashMap<String, String> params) {
        System.out.println(params);
        try {
            Cart cart = new Cart();
            cart.setNum(Integer.valueOf(params.get("num")));
            cart.setCakeId(Integer.valueOf(params.get("cakeId")));
            return cartService.addCart(cart, params.get("username"), true);
        } catch (Exception exception) {
            return JsonTool.createJsonData(Result.error("加入购物车失败!用户名或商品不存在"));
        }
    }

    /**
     * 购物车页面增加调用接口
     *
     * @param params
     * @return
     */
    @PostMapping(value = "/shoppingCart", produces = "application/json;charset=utf-8")
    public String addShoppingCart(@RequestBody HashMap<String, String> params) {
        System.out.println(params);
        try {
            Cart cart = new Cart();
            cart.setNum(Integer.valueOf(params.get("num")));
            cart.setCakeId(Integer.valueOf(params.get("cakeId")));
            return cartService.addCart(cart, params.get("username"), false);
        } catch (Exception exception) {
            return JsonTool.createJsonData(Result.error("加入购物车失败!用户名或商品不存在"));
        }
    }

    @PostMapping(value = "/cleanOneCart", produces = "application/json;charset=utf-8")
    public String cleanOneCart(@RequestBody HashMap<String, String> params) {
        System.out.println(params);
        try {
            Cart cart = new Cart();
            cart.setNum(0);
            cart.setCakeId(Integer.valueOf(params.get("cakeId")));
            return cartService.cleanOne(cart, params.get("username"));
        }catch (Exception exception){
            return JsonTool.createJsonData(Result.error("删除此项购物车失败!参数错误"));
        }
    }


    @GetMapping(value = "/getCartInfo", produces = "application/json;charset=utf-8")
    public String getAllCartByUsername(@RequestParam String username) {
        return cartService.getAllCartByUsername(username);
    }

    @GetMapping(value = "/cleanAllCart", produces = "application/json;charset=utf-8")
    public String cleanAllCartByUsername(@RequestParam String username) {
        return cartService.cleanAllCartByUsername(username);
    }


//
//    @DeleteMapping(produces = "application/json;charset=utf-8")
//    public String deleteCake(@RequestBody HashMap<String, List<Integer>> params) {
//        List<Integer> ids = params.get("idList");
//        return cakeService.deleteByIds(ids);
//    }
//
//    @PutMapping(produces = "application/json;charset=utf-8")
//    public String modifyCake(@RequestBody Cake Cake) {
//        return cakeService.updateCake(Cake);
//    }
//
//
//    @PostMapping(value = "/choose", produces = "application/json;charset=utf-8")
//    public String getData(@RequestBody HashMap<String, String> params) {
//        System.out.println(params);
//        int currentPage = Integer.parseInt(params.get("currentPage"));
//        int pageSize = Integer.parseInt(params.get("pageSize"));
//        Cake Cake = new Cake();
//        Cake.setName(params.get("name"));
//        Cake.setStatus(params.get("status"));
//        if (params.get("cid") != null) Cake.setCid(params.get("cid"));
//        System.out.println(pageSize);
//        return cakeService.conditionQuery(Cake, currentPage, pageSize);
//    }
}
