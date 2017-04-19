package com.shop.Controller;

import com.google.gson.Gson;
import com.shop.Pojo.Product;
import com.shop.Pojo.Transaction;
import com.shop.Service.ProductService;
import com.shop.Service.TransactionService;
import com.shop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Carpenter on 2017/3/6.
 */

@Controller
@RequestMapping(value = "/buy")
public class BuyController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @RequestMapping(value = "/submitOrder", method = RequestMethod.GET)
    public
    @ResponseBody
    String submitOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);
        Gson res = new Gson();
        Transaction transaction = new Transaction();
        boolean flag = false;
        if (httpSession != null && redisOperationsSessionRepository.getSession(httpSession.getId()) != null &&
                httpSession.getAttribute("user_id") != null) {
            try {
                int pid = Integer.parseInt(request.getParameter("pid"));
                int amount = Integer.parseInt(request.getParameter("amount"));
                int user_id = (int) httpSession.getAttribute("user_id");
                //String address = request.getParameter("address");
                double price = Double.parseDouble(request.getParameter("price"));
                if (pid >=1 && amount >=1 && user_id >= 1 && price > 0) {
                    transaction.setUser_id(user_id);
                    transaction.setProduct_id(pid);
                    transaction.setAmount(amount);
                    transaction.setAddress("重庆");
                    transaction.setPrice(price);
                    transaction.setX(117);
                    transaction.setY(30);
                    flag = transactionService.insert(transaction);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (flag ? "{\"state\" : 1}" : "{\"state\" : 0}");
    }

    @RequestMapping(value = "/getHistoryOrder", method = RequestMethod.GET)
    public
    @ResponseBody
    String getOrderHistory(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);
        Gson res = new Gson();
        List<Transaction> list = null;
        if (httpSession != null && redisOperationsSessionRepository.getSession(httpSession.getId()) != null &&
                httpSession.getAttribute("user_id") != null) {
            try {
                int user_id = (int) httpSession.getAttribute("user_id");
                if (user_id >= 1) {
                    list = transactionService.selectAll(user_id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (list != null) {
            try {
                for (int i = 0; i < list.size(); i++) {
                    int pid = list.get(i).getProduct_id();
                    Product product = productService.getProductById(pid);
                    list.get(i).setTitle(product.getTitle());
                    list.get(i).setThumbnail(product.getThumbnail());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (list == null ? "[]" : res.toJson(list));
    }

}
