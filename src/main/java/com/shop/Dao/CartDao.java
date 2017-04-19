package com.shop.Dao;

import com.shop.Pojo.Cart;
import org.apache.ibatis.annotations.*;
import org.springframework.security.access.method.P;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/8.
 */
@Mapper
public interface CartDao {
    @Insert("INSERT INTO cart(user_id, product_id, amount) VALUES(#{user_id}, #{product_id}, #{amount})")
    boolean insert(Cart cart);

    @Delete("DELETE FROM cart WHERE user_id = #{user_id} AND product_id = #{product_id}")
    boolean delete(@Param("user_id") int user_id, @Param("product_id") int product_id);

    @Select("SELECT * From cart WHERE user_id = #{user_id}")
    List<Cart> getAllCartRecordsByID(int user_id);

    @Select(("SELECT * FROM cart WHERE user_id = #{user_id} AND product_id = #{product_id}"))
    Cart getRecordByID(@Param("user_id") int user_id, @Param("product_id") int product_id);

    @Update("UPDATE cart SET amount = #{amount} WHERE user_id = #{user_id} AND product_id = #{product_id}")
    boolean updateAmount(Cart cart);
}
