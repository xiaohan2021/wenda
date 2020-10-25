package com.nowcoder.service;

import com.nowcoder.controller.LoginController;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.User;
import com.nowcoder.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/24
 */
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    @Autowired
    UserDAO userDAO;

    public User getUser(int id){
        return userDAO.selectById(id);
    }

    public Map<String, String> register(String username, String password){
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isBlank(username)){
            map.put("msg", "用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user != null){
            map.put("msg", "用户名已经被注册");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        return map;
    }
}
