package com.shop.Assist;

import com.shop.Pojo.Product;
import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Carpenter on 2017/3/8.
 */
public class ProductSQL {
    public String update(final Product product) {
        return new SQL() {
            {
                UPDATE("product");

                if (product.getTitle() != null) {
                    SET("title = #{title}");
                }
                if (product.getCatalog() != null) {
                    SET("catalog = #{catalog}");
                }
                if (product.getPrice() != 0) {
                    SET("price = #{price}");
                }

                WHERE("id = #{id}");

            }
        }.toString();
    }

}
