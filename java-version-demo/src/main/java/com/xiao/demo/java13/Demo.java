package com.xiao.demo.java13;

public class Demo {

    public static void main(String[] args) {

//        stringTemplateDemo();

        System.out.println(descLanguage("a"));

    }


    /**
     * Switch 表达式中就多了一个关键字用于跳出 Switch 块的关键字 yield，主要用于返回一个值
     * yield和 return 的区别：
     *     return 会直接跳出当前循环或者方法
     *     yield 只会跳出当前 Switch 块，同时在使用 yield 时，需要有 default 条件
     */
    static String descLanguage(String name) {
        return switch (name.toUpperCase()) {
            case "JAVA": yield "object-oriented, platform independent and secured";
            case "RUBY": yield "a programmer's best friend";
            default: yield name +" is a good language";
        };
    }


    // 文本块是预览的新特性，在未来可能会移除掉
    static void stringTemplateDemo() {
        String json1 =
                "{\n" +
                "   \"name\":\"mkyong\",\n" +
                "   \"age\":38\n" +
                "}\n";

        String json2 = """
                {
                    "name":"mkyong",
                    "age":38
                }
                """;


        String query1 =
                "SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`\n" +
                "WHERE `CITY` = 'INDIANAPOLIS'\n" +
                "ORDER BY `EMP_ID`, `LAST_NAME`;\n";

        String query2 = """
               SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`
               WHERE `CITY` = 'INDIANAPOLIS'
               ORDER BY `EMP_ID`, `LAST_NAME`;
               """;


        System.out.println(json1);
        System.out.println(json2);
        System.out.println(query1);
        System.out.println(query2);

    }
}
