package com.xiao.demo.java11;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Demo {

    public static void main(String[] args) {

        httpClientDemo();

//        stringDemo();

    }


    //
    static void httpClientDemo() {

        HttpRequest request =  HttpRequest.newBuilder()
                .uri(URI.create("http://www.baidu.com"))
                .GET()
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        try {
            // 同步
//            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            System.out.println(response.body());

            // 异步
            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(System.out::println);

            Thread.sleep(1500);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void stringDemo() {

        // 判断字符串是否为空
        String str = "   ";
        System.out.println(str.isBlank()); // true

        // 去除字符串首尾空格
        System.out.println(" java ".strip()); // "java"

        // 去除字符串首部空格
        System.out.println(" java ".stripLeading()); // "java "

        // 去除字符串尾部空格
        System.out.println(" java ".stripTrailing()); // " java"

        // 重复字符串多少次
        System.out.println("java".repeat(3)); // "javajavajava"

    }
}
