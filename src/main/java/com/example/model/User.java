package com.example.model;

/**
 * @Description:
 * @Author: 小韩同学
 * @Date: 2020/10/23
 */
public class User {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name) {
        this.name = name;
    }

    public String getDescription (){
        return "This is " + name;
    }
}
