package com.xiao.demo.java9;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Demo {

    public static void main(String[] args) {



    }


    static void tryWithResource() {
        // java9 之前
        try (Scanner scanner = new Scanner(new File(""));
             PrintWriter writer = new PrintWriter(new File(""))) {
        } catch (Exception e) {
            e.printStackTrace();
        }

        // java9 之后
        try {
            Scanner scanner = new Scanner(new File(""));
            PrintWriter writer = new PrintWriter(new File(""));
            try (scanner; writer) { // 会自动关闭 scanner，writer
                while (scanner.hasNext()) {
                    writer.println(scanner.nextLine());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("文件没找到");
        }

    }

    // java9 提供快速创建不可变集合的工厂方法
    static void CollectionQuickCreate() {
         List<String> list = List.of("a", "b", "c");
         Set<String> set = Set.of("a", "b", "c");
         Map<String, String> map = Map.of("k1", "v1", "k2", "v2");
    }
}
