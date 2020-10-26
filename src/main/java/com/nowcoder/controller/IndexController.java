package com.nowcoder.controller;

import com.nowcoder.model.User;
import com.nowcoder.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/22
 */
//@Controller
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    public WendaService wendaService;
    /**
     * 首页
     * @param session
     * @return
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession session) {
        logger.info("VISIT HOME");
        return wendaService.getMessage(2) + "hello world  " + session.getAttribute("msg");
    }

    /**
     * 路径参数和请求参数
     * @param userId
     * @param groupId
     * @param type
     * @param key
     * @return
     */
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", required = false) String key) {
        return String.format("Profile Page of %s / %d, t:%d k: %s", groupId, userId, type, key);
    }

    /**
     * velocity模板渲染
     * @param model
     * @return
     */
    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})
    public String template(Model model) {
        model.addAttribute("value1", "vvvvv1");
        List<String> colors = Arrays.asList(new String[]{"RED", "GREEN", "BLUE"});
        model.addAttribute("colors", colors);

        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("LEE"));
        return "home";
    }

    /**
     * request:参数解析、cookie读取、http请求字段、文件上传
     * response：页面返回内容、cookie下发、http字段设置、headers
     * @param model
     * @param response
     * @param request
     * @param session
     * @param sessionId
     * @return
     */
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String request(Model model, HttpServletResponse response,
                           HttpServletRequest request,
                           HttpSession session,
                           @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("COOKIEVALUE: " + sessionId + "<br>");
        Enumeration<String> headersNames = request.getHeaderNames();
        while(headersNames.hasMoreElements()){
            String name = headersNames.nextElement();
            sb.append(name + ":" + request.getHeader(name) + "<br>");
        }

        if(request.getCookies() != null){
            for(Cookie cookie : request.getCookies()){
                sb.append("Cookie:" + cookie.getName() + " value " + cookie.getValue() );
            }
        }

        sb.append(request.getMethod() + "<br>");
        sb.append(request.getQueryString() + "<br>");
        sb.append(request.getPathInfo() + "<br>");
        sb.append(request.getRequestURI() + "<br>");

        response.addHeader("nowCoderId", "hello" );
        response.addCookie(new Cookie("username", "nowCoder"));


        return sb.toString();
    }

    /**
     * 重定向 301：永久转移 302：临时转移
     * @param code
     * @param session
     * @return
     */
    @RequestMapping(path = {"/redirect/{code}"}, method = {RequestMethod.GET})
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession session){
        session.setAttribute("msg", "jump from redirect");
        RedirectView red = new RedirectView("/",true);
        if(code == 301){
            red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return red;
    }

    /**
     * 异常演示
     * @param key
     * @return
     */
    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if("admin".equals(key)){
            return "hello admin";
        }
        throw new IllegalArgumentException("参数不对");
    }

    /**
     * 统一异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
}
