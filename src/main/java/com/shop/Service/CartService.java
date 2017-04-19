package com.shop.Service;

import com.shop.Dao.CartDao;
import com.shop.Pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/8.
 */
@Service
public class CartService {
    @Autowired
    private CartDao cartDao;

    public boolean insert(int user_id, int product_id, int amount){
        try {
            Cart cart = new Cart();
            cart.setUser_id(user_id);
            cart.setProduct_id(product_id);
            cart.setAmount(amount);
            if(amount == 0) return false;
            return cartDao.insert(cart);
        }catch (Exception e){
            return false;
        }
    }

    public boolean delete(int user_id, int product_id){
        try{
            return cartDao.delete(user_id, product_id);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Cart> getCartRecordsByID(int user_id){
        try{
            return cartDao.getAllCartRecordsByID(user_id);
        }catch (Exception e){
            return null;
        }
    }

    public Cart getRecordByID(int user_id, int product_id){
        try {
            return cartDao.getRecordByID(user_id, product_id);
        }catch (Exception e){
            return null;
        }
    }

    public boolean updateAmount(int user_id, int product_id, int amount){
        try{
            Cart cart = new Cart();
            cart.setUser_id(user_id);
            cart.setProduct_id(product_id);
            cart.setAmount(amount);
            if(amount == 0) return false;
            return cartDao.updateAmount(cart);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
