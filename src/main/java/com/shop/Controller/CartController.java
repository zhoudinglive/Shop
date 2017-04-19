package com.shop.Controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shop.Pojo.Cart;
import com.shop.Pojo.Product;
import com.shop.Service.CartService;
import com.shop.Service.ProductService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Carpenter on 2017/3/8.
 */
@Controller
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @RequestMapping(value = "/addCart", method = RequestMethod.GET)
    public @ResponseBody String addCart(HttpServletRequest request, HttpServletResponse response) {
        try{
            HttpSession httpSession = request.getSession(false);
            Cookie[] cookies = request.getCookies();
            String product_id = request.getParameter("product_id").replaceAll(" ", "");
            String amount = request.getParameter("amount").replaceAll(" ", "");
            if (httpSession == null || redisOperationsSessionRepository.getSession(httpSession.getId()) == null ||
                    httpSession.getAttribute("user_id") == null) {
                if (product_id == null || amount == null || !isNum(product_id) || !isNum(amount)) {
                    return "{\"state\" : 0}";
                }
                if(cookies != null){
                    for (int i = 0; i < cookies.length; ++i) {
                        if (cookies[i].getName().equals("CartRec")) {
                            cookies[i].setValue(cookies[i].getValue() + "#" + product_id + "/" + amount);
                            response.addCookie(cookies[i]);
                            return "{\"state\" : 1}";
                        }
                    }
                }
                Cookie cookie = new Cookie("CartRec", product_id + "/" + amount);
                cookie.setMaxAge(24 * 60 * 60 * 7);
                response.addCookie(cookie);
                return "{\"state\" : 1}";
            } else {

                int uid = (int) httpSession.getAttribute("user_id");

                if (cookies != null && cookies.length != 0) {
                    for (int i = 0; i < cookies.length; ++i) {
                        if (cookies[i].getName().equals("CartRec")) {
                            String temp[] = cookies[i].getValue().split("#");
                            for (int j = 0; j < temp.length; ++j) {
                                String[] ids = temp[j].replaceAll(" ", "").split("/");
                                Cart cart = cartService.getRecordByID(uid, Integer.parseInt(ids[0]));
                                if (cart == null) {
                                    cartService.insert(uid, Integer.parseInt(ids[0]), Integer.parseInt(ids[1]));
                                } else {
                                    cartService.updateAmount(uid, Integer.parseInt(ids[0]), cart.getAmount() + Integer.parseInt(ids[1]));
                                }
                            }
                            cookies[i].setMaxAge(0);
                            response.addCookie(cookies[i]);
                            break;
                        }
                    }
                }
                if (product_id != null && amount != null && isNum(product_id) && isNum(amount)) {
                    int pid = Integer.parseInt(product_id);
                    Cart cart = cartService.getRecordByID(uid, pid);
                    if (cart == null) {
                        cartService.insert(uid, pid, Integer.parseInt(amount));
                    } else {
                        cartService.updateAmount(uid, pid, cart.getAmount() + Integer.parseInt(amount));
                    }
                }
                return "{\"state\" : 1}";
            }
        }catch(Exception e){
            return "{\"state\" : 0}";
        }
    }

    @RequestMapping(value = "/getMyCart", method = RequestMethod.GET)
    public @ResponseBody String getMyCart(HttpServletRequest request, HttpServletResponse response){
        HttpSession httpSession = request.getSession(false);
        Cookie[] cookies = request.getCookies();
        List<Cart> carts = new ArrayList<Cart>();
        if(httpSession == null || redisOperationsSessionRepository.getSession(httpSession.getId()) == null ||
                httpSession.getAttribute("user_id") == null){
            if(cookies != null && cookies.length != 0){
                for(int i = 0; i < cookies.length; ++i){
                    if(cookies[i].getName().equals("CartRec")){
                        if(cookies[i].getValue().equals("")) return "[]";
                        String temp[] = cookies[i].getValue().split("#");
                        Map<String, Integer> map = new HashMap<String, Integer>();
                        for(int j = 0; j < temp.length; ++j){
                            String[] ids = temp[j].replaceAll(" ", "").split("/");
                            if(ids.length <= 1) continue;
                            if(!map.containsKey(ids[0])){
                                map.put(ids[0], Integer.parseInt(ids[1]));
                            }else{
                                map.put(ids[0], map.get(ids[0]) + Integer.parseInt(ids[1]));
                            }
                        }
                        if(map.size() == 0) return "{\"state\" : 0}";
                        String ans = "[";
                        for(Map.Entry<String, Integer> entry : map.entrySet()){
                            int pid = Integer.parseInt(entry.getKey());
                            int amount = entry.getValue();
                            Product product = productService.getProductById(pid);
                            if(product != null){
                                ans += "{\"title\" : \"" + product.getTitle() + "\", " +
                                        "\"price\" : \"" + product.getPrice() + "\", " +
                                        "\"thumbnail\" : \"" + product.getThumbnail() + "\", " +
                                        "\"amount\" : \"" + amount +"\", " +
                                        "\"product_id\" : \"" + product.getId() + "\"},";
                            }
                        }
                        ans = ans.substring(0, ans.length() - 1) + "]";
                        return ans;
                    }
                }
            }
            return "{\"state\" : 0}";
        }else{
            int uid = (int)httpSession.getAttribute("user_id");
            carts = cartService.getCartRecordsByID(uid);
            if(carts == null || carts.size() == 0) return "{\"state\" : 0}";
            String ans = "[";
            for(int i = 0; i < carts.size(); ++i){
                int pid = carts.get(i).getProduct_id();
                int amount = carts.get(i).getAmount();
                Product product = productService.getProductById(pid);
                if(product != null){
                    ans += "{\"title\" : \"" + product.getTitle() + "\", " +
                            "\"price\" : \"" + product.getPrice() + "\", " +
                            "\"thumbnail\" : \"" + product.getThumbnail() + "\", " +
                            "\"amount\" : \"" + amount +"\", " +
                            "\"product_id\" : \"" + product.getId() + "\"},";
                }
            }
            ans = ans.substring(0, ans.length() - 1) + "]";
            return ans;
        }

    }

    @RequestMapping(value = "/deleteCart", method = RequestMethod.GET)
    public @ResponseBody String deleteCart(HttpServletRequest request, HttpServletResponse response){
        HttpSession httpSession = request.getSession(false);
        Cookie cookies[] = request.getCookies();
        String pid = request.getParameter("product_id").replaceAll(" ", "");
        if(pid == null || !isNum(pid)) return "{\"state\" : 0}";
        if(httpSession == null || redisOperationsSessionRepository.getSession(httpSession.getId()) == null ||
                httpSession.getAttribute("user_id") == null){
            if(cookies != null && cookies.length != 0){
                for(int i = 0; i < cookies.length; ++i){
                    if(cookies[i].getName().equals("CartRec")){
                        String[] temp = cookies[i].getValue().split("#");
                        if(temp.length == 0) return "{\"state\" : 0}";
                        String ans = "";
                        for(int j = 0; j < temp.length; ++j){
                            String[] ids = temp[j].replaceAll(" ", "").split("/");
                            if(!ids[0].equals(pid)){
                                ans += temp[j] + "#";
                            }
                        }
                        cookies[i].setValue(ans);
                        response.addCookie(cookies[i]);
                        return "{\"state\" : 1}";
                    }
                }
            }
            return "{\"state\" : 0}";
        }else{
            int uid = (int) httpSession.getAttribute("user_id");
            if(cartService.delete(uid, Integer.parseInt(pid))){
                return "{\"state\" : 1}";
            }
            return "{\"state\" : 0}";
        }
    }

    @RequestMapping(value = "/updateAmount", method = RequestMethod.GET)
    public @ResponseBody String updateAmount(HttpServletRequest request, HttpServletResponse response){
        HttpSession httpSession = request.getSession(false);
        Cookie cookies[] = request.getCookies();
        String pid = request.getParameter("product_id").replaceAll(" ", "");
        String amount = request.getParameter("amount").replaceAll(" ", "");
        if(pid == null || amount == null || !isNum(pid) || !isNum(amount)) return "{\"state\" : 0}";
        if(httpSession == null || redisOperationsSessionRepository.getSession(httpSession.getId()) == null ||
                httpSession.getAttribute("user_id") == null){
            if(cookies != null && cookies.length != 0){
                for(int i = 0; i < cookies.length; ++i){
                    if(cookies[i].getName().equals("CartRec")){
                        String[] temp = cookies[i].getValue().split("#");
                        String ans = amount.equals("0") ? "" :pid + "/" + amount + "#";
                        for(int j = 0; j < temp.length; ++j){
                            String[] ids = temp[j].replaceAll(" ", "").split("/");
                            if(!ids[0].equals(pid)){
                                ans += temp[j];
                            }
                        }
                        cookies[i].setValue(ans);
                        response.addCookie(cookies[i]);
                        return "{\"state\" : 1}";
                    }
                }
            }
            return "{\"state\" : 0}";
        }else{
            int uid = (int) httpSession.getAttribute("user_id");
            int a = Integer.parseInt(amount);
            if(a <= 0){
                return "{\"state\" : 0}";
            }
            if(cartService.updateAmount(uid, Integer.parseInt(pid), a)){
                return "{\"state\" : 1}";
            }
            return "{\"state\" : 0}";
        }
    }

    private boolean isNum(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNUM = pattern.matcher(str);
        if(!isNUM.matches()){
            return false;
        }
        return true;
    }
}
