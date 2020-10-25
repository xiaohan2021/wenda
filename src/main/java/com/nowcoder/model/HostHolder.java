package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/25
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>(); // 公共接口 为每个线程分配不一样的user对象

    public User getUser(){
        return users.get();
    }

    public void serUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
