package com.nowcoder.controller;

import com.nowcoder.service.WendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/23
 */@Controller
public class SettingController {

    @Autowired
    public WendaService wendaService;
    /**
     * 设置
     * @return
     */
    @RequestMapping(path = {"/setting"}, method = {RequestMethod.GET})
    @ResponseBody
    public String setting(HttpSession session) {
        return "Setting ok." + wendaService.getMessage(1) ;
    }

}
