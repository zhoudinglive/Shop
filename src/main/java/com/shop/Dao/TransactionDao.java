package com.shop.Dao;

import com.shop.Pojo.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Mapper
public interface TransactionDao {
    @Insert("INSERT INTO `transaction` (`user_id`, `product_id`, `amount`, `address`, `price`, `location`) " +
            "VALUES (#{user_id}, #{product_id}, #{amount}, #{address}, #{price},point(#{x}, #{y}));")
    boolean insert(Transaction transaction);

    @Delete("DELETE FROM `transaction` WHERE `id` = #{id};")
    boolean delete(@Param("id") int id);

    @Select("SELECT x(`location`) AS x, y(`location`) AS y FROM `transaction` WHERE `id` = #{id};")
    Transaction getLocation(@Param("id") int id);

    @Select("SELECT * FROM `transaction` WHERE `user_id` = #{user_id};" )
    List<Transaction> getTransactionByUser(int user_id);
}
