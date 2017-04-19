package com.shop.Controller;

import com.google.gson.Gson;
import com.shop.Pojo.Product;
import com.shop.Pojo.Recommend;
import com.shop.Pojo.User;
import com.shop.Service.RecommendService;
import com.shop.Service.UserService;
import com.shop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.List;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final int singlePageCount = 12;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @RequestMapping(value = "/rank", method = RequestMethod.GET)
    public
    @ResponseBody
    String rank(HttpServletRequest request, HttpServletResponse response) {
        Gson gson = new Gson();
        return "rank";
    }

    @RequestMapping(value = "/searchCount", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String searchCount(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("keyword");
        int res = 0;
        try {
            if (keyword != null && !keyword.equals("")) {
                res = productService.getProductCountByTitle(keyword);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(res);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String search(HttpServletRequest request, HttpServletRequest response) {
        String keyword = request.getParameter("keyword");
        //System.out.println(keyword);
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String pageParam = request.getParameter("page");
        if (keyword != null && !keyword.equals("") && pageParam != null && !pageParam.equals("")) {
            Gson res = new Gson();
            List<Product> list = null;
            try {
                //keyword = URLDecoder.decode(keyword, "UTF-8");
                //System.out.println(keyword);
                int page = Integer.parseInt(pageParam);
                if (page >= 1) {
                    int start = (page - 1) * singlePageCount;
                    if (sort != null && sort.equals("sale")) {
                        if (order != null && order.equals("desc")) {
                            list = productService.getProductByTitle_saleDesc(keyword, start, singlePageCount);
                        } else if (order != null && order.equals("asc")) {
                            list = productService.getProductByTitle_saleAsc(keyword, start, singlePageCount);
                        }
                    } else if (sort != null && sort.equals("score")) {
                        if (order != null && order.equals("desc")) {
                            list = productService.getProductByTitle_scoreDesc(keyword, start, singlePageCount);
                        } else if (order != null && order.equals("asc")) {
                            list = productService.getProductByTitle_scoreAsc(keyword, start, singlePageCount);
                        }
                    } else if (sort != null && sort.equals("price")) {
                        if (order != null && order.equals("desc")) {
                            list = productService.getProductByTitle_priceDesc(keyword, start, singlePageCount);
                        } else if (order != null && order.equals("asc")) {
                            list = productService.getProductByTitle_priceAsc(keyword, start, singlePageCount);
                        }
                    } else {
                        list = productService.getProductByTitle_id(keyword, start, singlePageCount);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.gc();
            return (list == null ? "[]" : res.toJson(list));
        } else {
            System.gc();
            return "[]";
        }
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public
    @ResponseBody
    String detail(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession httpSession = request.getSession(false);
            if (httpSession != null && redisOperationsSessionRepository.getSession(httpSession.getId()) != null) {
                int user_id = (int) httpSession.getAttribute("user_id");
                int pid = Integer.parseInt(request.getParameter("pid"));
                Recommend recommend = new Recommend();
                recommend.setUser_id(user_id);
                recommend.setProduct_id(pid);
                int curMark = recommendService.getMark(user_id, pid);
                if (curMark == -1) {
                    recommend.setMark(1);
                    recommendService.insert(recommend);
                } else {
                    recommend.setMark(curMark + 1);
                    recommendService.updateMark(recommend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson res = new Gson();
        Product product = null;
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            if (pid >= 1) {
                product = productService.getProductById(pid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
        return (product == null ? "{}" : res.toJson(product));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody String register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");
        if(username == null || password1 == null || password2 == null || !password1.equals(password2)){
            return "{\"state\": 01}";
        }else{
            if(userService.select(username) != null){
                return "{\"state\": 02}";
            }else{
                User user = new User();
                user.setUsername(username);
                user.setPassword(password1);
                user.setRole("ROLE_USER");
                if(userService.insert(user)){
                    return "{\"state\": 1}";
                }
                return "{\"state\": 03}";
            }
        }
    }

}
