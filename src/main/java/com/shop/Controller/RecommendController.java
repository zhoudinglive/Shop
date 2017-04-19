package com.shop.Controller;

import com.google.gson.Gson;
import com.shop.Pojo.Cfans;
import com.shop.Pojo.Product;
import com.shop.Service.CfansService;
import com.shop.Service.ProductService;
import com.shop.Service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Carpenter on 2017/3/13.
 */
@Controller
@RequestMapping(value = "/recommend")
public class RecommendController {
    @Autowired
    private RecommendService recommendService;

    @Autowired
    private CfansService cfansService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    String test(HttpServletRequest request) {
        if (recommendService.userCF()) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping(value = "/getItems", method = RequestMethod.GET)
    public @ResponseBody
    String getItems(HttpServletRequest request) {
        List<Cfans> list = null;
        Gson res = new Gson();
        HttpSession httpSession = request.getSession(false);
        if (httpSession != null && redisOperationsSessionRepository.getSession(httpSession.getId()) != null) {
            try {
                int uid = (int) httpSession.getAttribute("user_id");
                list = cfansService.getAllRecommendByUid(uid);
                for (int i = 0; i < list.size(); i++) {
                    System.out.println("tuijian" + list.get(i).getPid());
                    Product product = productService.getProductById(list.get(i).getPid());
                    list.get(i).setTitle(product.getTitle());
                    list.get(i).setThumbnail(product.getThumbnail());
                    list.get(i).setPrice(product.getPrice());
                    list.get(i).setSale(product.getSale());
                    list.get(i).setScore(product.getScore());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return (list == null ? "[]" : res.toJson(list));
    }

}
