package com.shop.Controller;

import com.shop.Dao.UserDao;
import com.shop.Pojo.Product;
import com.shop.Pojo.Transaction;
import com.shop.Service.ProductService;
import com.shop.Service.TransactionService;
import com.shop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Carpenter on 2017/3/6.
 */
@Controller
@RequestMapping(value = "/admin")
public class AdminController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/addProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    String addProduct(HttpServletRequest request, HttpServletResponse response) {
        boolean flag = false;
        Product product = new Product();
        try {
            String title = request.getParameter("title");
            String catalog = request.getParameter("catalog");
            double price = Double.parseDouble(request.getParameter("price"));
            String thumbnail = request.getParameter("thumbnail");
            String tag = request.getParameter("tag");
            String detail = request.getParameter("detail");
            if (title != null && !title.equals("") && catalog != null && !catalog.equals("") && thumbnail != null && !thumbnail.equals("") && detail != null & !detail.equals("")) {
                product.setTitle(title);
                product.setCatalog(catalog);
                product.setPrice(price);
                product.setThumbnail(thumbnail);
                product.setTag(tag);
                product.setDetail(detail);
                flag = productService.insert(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (flag ? "{\"state\" : 1}" : "{\"state\" : 0}");
    }

    @RequestMapping(value = "/deleteProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    String deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        boolean flag = false;
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            if (pid >= 1) {
                flag = productService.delete(pid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (flag ? "{\"state\" : 1}" : "{\"state\" : 0}");
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.GET)
    public
    @ResponseBody
    String updateProduct(HttpServletRequest request, HttpServletResponse response) {
        boolean flag = false;
        Product product = new Product();
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            String title = request.getParameter("title");
            String catalog = request.getParameter("catalog");
            double price = Double.parseDouble(request.getParameter("price"));
            String tag = request.getParameter("tag");
            if (pid >= 1 && title != null && !title.equals("") && catalog != null && !catalog.equals("")) {
                product.setId(pid);
                product.setTitle(title);
                product.setCatalog(catalog);
                product.setPrice(price);
                product.setTag(tag);
                flag = productService.update(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (flag ? "{\"state\" : 1}" : "{\"state\" : 0}");
    }

}
