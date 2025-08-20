package com.xiao.springbootdmdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiao.springbootdmdemo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
