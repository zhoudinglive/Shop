package com.shop.Dao;

import com.shop.Pojo.Recommend;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by Carpenter on 2017/3/7.
 */
@Mapper
public interface RecommendDao {
    @Insert("INSERT INTO recommend(user_id, product_id, mark)" +
            "VALUES(#{user_id}, #{product_id}, #{mark})")
    boolean insert(Recommend recommend);

    @Delete("DELETE FROM recommend where user_id = #{user_id} and product_id = #{product_id}")
    boolean delete(@Param("user_id") int user_id, @Param("product_id") int product_id);

    @Select("SELECT * FROM recommend WHERE user_id = #{user_id}")
    List<Recommend> getRecommendByUserId(int user_id);

    @Select("SELECT `mark` FROM `recommend` WHERE `user_id` = #{user_id} AND `product_id` = #{product_id}")
    Recommend getMark(@Param("user_id") int user_id, @Param("product_id") int product_id);

    @Update("UPDATE recommend SET mark = #{mark} WHERE user_id = #{user_id} AND product_id = #{product_id}")
    boolean updateMark(Recommend recommend);
}
