package com.xiao.springbootdmdemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "\"USER\"")
public class User {

    @TableField("ID")
    private Integer id;

    private String name;

    private Integer age;

    private String address;

}
