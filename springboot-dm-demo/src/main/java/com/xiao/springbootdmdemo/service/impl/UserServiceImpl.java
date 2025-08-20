package com.xiao.springbootdmdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiao.springbootdmdemo.entity.User;
import com.xiao.springbootdmdemo.mapper.UserMapper;
import com.xiao.springbootdmdemo.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


}
