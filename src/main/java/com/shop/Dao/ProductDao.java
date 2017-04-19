package com.shop.Dao;

import com.shop.Assist.ProductSQL;
import com.shop.Pojo.Product;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface ProductDao {
    @Insert("INSERT INTO `product` (`title`, `catalog`, `price`, `thumbnail`, `tag`, `detail`) " +
            "VALUES (#{title}, #{catalog}, #{price}, #{thumbnail}, #{tag}, #{detail});")
    boolean insert(Product product);

    @Delete("DELETE FROM `product` WHERE `id`=#{id};")
    boolean delete(int id);

    @Select("SELECT COUNT(*) FROM `product` WHERE `title` LIKE #{title};")
    int getProductCountByTitle(@Param("title") String title);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `id` ASC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_id(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `sale` DESC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_saleDesc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `sale` ASC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_saleAsc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `score` DESC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_scoreDesc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `score` ASC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_scoreAsc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `price` DESC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_priceDesc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` " +
            "WHERE `title` LIKE #{title} ORDER BY `price` ASC LIMIT #{start}, #{num};")
    List<Product> getProductsByTitle_priceAsc(@Param("title") String title, @Param("start") int start, @Param("num") int num);

    @Select("SELECT * FROM `product` WHERE `id`=#{id};")
    Product getProductByID(int id);

    @UpdateProvider(type = ProductSQL.class, method = "update")
    boolean update(Product product);
}



