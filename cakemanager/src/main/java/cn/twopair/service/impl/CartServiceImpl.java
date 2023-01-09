package cn.twopair.service.impl;

import cn.twopair.mapper.CakeMapper;
import cn.twopair.mapper.CartMapper;
import cn.twopair.mapper.UserMapper;
import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Cart;
import cn.twopair.result.Code;
import cn.twopair.result.Result;
import cn.twopair.result.ShoppingCartResult;
import cn.twopair.service.CartService;
import cn.twopair.util.JsonTool;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final UserMapper userMapper;
    private final CakeMapper cakeMapper;

    public CartServiceImpl(CartMapper cartMapper, UserMapper userMapper, CakeMapper cakeMapper) {
        this.cartMapper = cartMapper;
        this.userMapper = userMapper;
        this.cakeMapper = cakeMapper;
    }

    @Override
    public String addCart(Cart cart, String username, boolean details) {
        try {

            Integer userId = userMapper.selectByUsername(username).getId();
            cart.setUserId(userId);

            Cart t = cartMapper.selectByUserIdAndCakeId(cart);
            if (t == null) {
                int ok = cartMapper.insert(cart);
                if (ok > 0) {
                    return JsonTool.createJsonData(Result.success("加入购物车成功!"));
                }
                return JsonTool.createJsonData(Result.error("加入购物车失败!"));
            }
            if (details) cart.setNum(cart.getNum() + t.getNum());

            int ok = cartMapper.update(cart);
            if (ok > 0) {
                return JsonTool.createJsonData(Result.success("加入购物车成功!"));
            }
            return JsonTool.createJsonData(Result.error("加入购物车失败!"));
        } catch (Exception exception) {
            System.out.println("CartServiceImpl异常");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonTool.createJsonData(Result.error("加入购物车失败!用户名或商品不存在"));
        }
    }

    @Override
    public String getAllCartByUsername(String username) {
        try {
            List<ShoppingCartResult> ret = new ArrayList<>();

            List<Cart> carts = cartMapper.selectByUsername(username);
            Integer userId = userMapper.selectByUsername(username).getId();
            for (Cart cart : carts) {
                ShoppingCartResult t = new ShoppingCartResult();
                t.setUserId(userId);
                t.setUsername(username);
                t.setCakeId(cart.getCakeId());
                t.setNum(cart.getNum());

                Cake cake = cakeMapper.selectById(cart.getCakeId());
                t.setCakeImgUrl(cake.getImgUrl());
                t.setCakeName(cake.getName());
                t.setPrice(cake.getPrice());

                ret.add(t);
            }
            return JsonTool.createJsonData(Result.ok(Code.createCode(Code.SUCCESS, "查询成功!"), ret, ret.size()));
        } catch (Exception exception) {
            return JsonTool.createJsonData(Result.success("无数据"));
        }

    }


    @Override
    public String cleanAllCartByUsername(String username) {
        try {
            int ok = cartMapper.deleteAllByUserId(userMapper.selectByUsername(username).getId());
            if (ok > 0) {
                return JsonTool.createJsonData(Result.success("清空购物车成功"));
            }
            return JsonTool.createJsonData(Result.warning("无购物车数据"));
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonTool.createJsonData(Result.error("清空购物车失败"));
        }
    }


    @Override
    public String cleanOne(Cart cart, String username) {
        try {
            Integer userId = userMapper.selectByUsername(username).getId();
            cart.setUserId(userId);

            int ok = cartMapper.deleteByUserIdAndCakeId(cart);
            if (ok > 0) {
                return JsonTool.createJsonData(Result.success("删除成功"));
            }
            return JsonTool.createJsonData(Result.warning("无此项购物车数据"));
        } catch (Exception exception) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return JsonTool.createJsonData(Result.error("删除此项购物车失败"));
        }

    }
}
