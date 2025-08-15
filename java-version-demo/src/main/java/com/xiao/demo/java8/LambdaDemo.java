package com.xiao.demo.java8;

public class LambdaDemo {

    public static void main(String[] args) {

        // java8 允许将函数作为方法参数，这个函数是一个匿名函数


        // 使用匿名内部类调用 lambdaTest 方法
        LambdaDemo.lambdaTest(new LambdaInterface() {
            @Override
            public void test(String param) {
                System.out.println("匿名内部类调用 lambdaTest 方法, 方法参数:" + param);
            }
        }, "param1");

        // 使用 lambda 表达式调用 lambdaTest 方法
        LambdaDemo.lambdaTest((param) -> System.out.println("lambda 表达式调用 lambdaTest 方法, 方法参数:" + param), "param2");


    }

    // 该方法参数是类型是接口，需要传一个接口的实现类
    static void lambdaTest(LambdaInterface l, String param) {
        l.test(param);
    }

}
