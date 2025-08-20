package com.xiao.springbootdmdemo.controller;

import com.xiao.springbootdmdemo.entity.User;
import com.xiao.springbootdmdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public void list() {
        List<User> list = userService.list();
        System.out.println(list);
    }

    @RequestMapping("/add")
    public void add() {
        User user = new User();
        user.setId(1);
        user.setName("张三");
        user.setAge(18);
        user.setAddress("xxxxxx");
        userService.save(user);

        User user1 = userService.getById(1);
        System.out.println(user1);
    }

}
