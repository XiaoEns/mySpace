package com.xiao.demo.java10;

import java.util.HashMap;
import java.util.List;

public class Demo {

    public static void main(String[] args) {
        varDemo();
    }


    // var, 局部变量关键字
    static void varDemo() {

        // var 的类型由编译器自动推断

        var num = 10;

        var var = 10;

        var str = "hello";

        var arr = new int[]{1, 2, 3};

        var list = List.of("a", "b", "c");

        var map = new HashMap<>();

        for (var i = 0; i < 10; i++) {
            System.out.printf("%d\t", i);
        }

        String str1 = "asdf";

        // 一些错误写法
        // 1. 直接声明，未初始化
//        var i;
        // 2. 不能为 null
//        var i = null;
        // 3. 不能声明数组
//        var arr = {1, 2, 3};

    }


}
