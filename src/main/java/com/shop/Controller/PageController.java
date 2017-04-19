package com.shop.Controller;

import com.shop.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by Carpenter on 2017/3/6.
 */
@Controller
public class PageController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperationsSessionRepository redisOperationsSessionRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(HttpServletRequest request){
        return "login";
    }

    @RequestMapping(value = "/checkIt", method = RequestMethod.POST)
    public @ResponseBody
    String loginCheck(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(true);
        if(httpSession == null || request.getParameter("username") == null) {
            return "{\"state\":0 }";
        }
        System.out.println(userService.select(request.getParameter("username")).getId());
        httpSession.setAttribute("user_id", userService.select(request.getParameter("username")).getId());
        httpSession.setAttribute("username", request.getParameter("username"));
        httpSession.setAttribute("password", request.getParameter("password"));
        return "{\"state\":1,\"username\":\"" + request.getParameter("username") + "\"}";
    }

    @RequestMapping(value = "/loginSession", method = RequestMethod.POST)
    public @ResponseBody String loginSession(HttpServletRequest request){
        HttpSession httpSession = request.getSession();
        if(httpSession != null && redisOperationsSessionRepository.getSession(httpSession.getId()) != null &&
                httpSession.getAttribute("user_id") != null){
            return "{\"state\": 1}";
        }
        return "{\"state\": 0}";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/searchList", method = RequestMethod.GET)
    public String searchList(){
        return "searchList";
    }

    @RequestMapping(value = "/shopCart", method = RequestMethod.GET)
    public String shopCart(){
        return "shopCart";
    }

    @RequestMapping(value = "/submitOrderPage", method = RequestMethod.GET)
    public String submitOrderPage(){
        return "submitOrderPage";
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String manage(){
        return "manage";
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String show() { return "show"; }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public String products() { return "products"; }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() { return "register"; }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    public String order() { return "order"; }
}
