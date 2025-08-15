package com.xiao.demo.java12;

import java.util.Scanner;

public class Demo {

    public static void main(String[] args) {

        instanceofDemo();

//        switchDemo()
    }


    static void instanceofDemo() {
        Object obj = "hello";

        // java12 之前，需要强制类型转换
        if (obj instanceof String) {
            String s1 = (String) obj;
            System.out.println("obj is String, value is " + s1);
        }

        // java12 之后，可以省略强制类型转换
        if (obj instanceof String str) {
            System.out.println("obj is String, value is " + str);
        }
    }



    static void switchDemo() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入一个数字：");
        int num = scanner.nextInt();


        // java12 之前，容易遗漏 break
        System.out.println("======java12 before============");
        switch (num) {
            case 1:
                System.out.println("num = " + num);
//                break;
            case 2:
                System.out.println("num = " + num);
//                break;
            default:
                System.out.println("default");

        }

        // java12 对 switch 进行了增强，可以省略 break
        System.out.println("======java12 after============");
        switch (num) {
            case 1 -> System.out.println("num = " + num);
            case 2 -> System.out.println("num = " + num);
            default -> System.out.println("default");
        }

    }

}
