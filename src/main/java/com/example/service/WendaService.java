package com.example.service;

import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/23
 */
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message : " + String.valueOf(userId);
    }
}
