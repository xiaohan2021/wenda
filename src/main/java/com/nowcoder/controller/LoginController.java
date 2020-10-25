package com.nowcoder.controller;

import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/24
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     * 注册功能
     * @param model
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password) {
        try {
            Map<String, String> map = userService.register(username, password);
            if(map.containsKey("msg")){
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            return "redirect:/";
        } catch (Exception e){
            logger.error("注册异常！！" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reg/"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String login(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password) {
        try {
            Map<String, String> map = userService.register(username, password);
            if(map.containsKey("msg")){
                model.addAttribute("msg", map.get("msg"));
                return "login";
            }
            return "redirect:/";
        } catch (Exception e){
            logger.error("注册异常！！" + e.getMessage());
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String reglogin(Model model) {
            return "login";
    }

}
