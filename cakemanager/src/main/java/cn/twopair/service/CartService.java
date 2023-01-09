package cn.twopair.service;

import cn.twopair.pojo.Cake;
import cn.twopair.pojo.Cart;
import org.springframework.transaction.annotation.Transactional;

public interface CartService {
    @Transactional
    String addCart(Cart cart, String username, boolean details);

    String getAllCartByUsername(String username);

    String cleanAllCartByUsername(String username);

    String cleanOne(Cart cart, String username);
}
