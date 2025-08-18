package com.xiao.demo.java14;

public class Demo {

    public static void main(String[] args) {

        Rectangle rectangle = new Rectangle(1.0f, 2.0f);
        System.out.println("length = " + rectangle.length() + "; width = " + rectangle.width());
        System.out.println(rectangle);

    }

}

/**
 * 预览特性
 * record 关键字可以简化 数据类（一个 Java 类一旦实例化就不能再修改）的定义方式，
 * 使用 record 代替 class 定义的类，只需要声明属性，就可以在获得属性的访问方法，以及 toString()，hashCode(), equals()方法。
 */
record Rectangle(float length, float width) { }


final class Rectangle2 {
    final float length;
    final float width;

    public Rectangle2(float length, float width) {
        this.length = length;
        this.width = width;
    }

    float length() { return length; }
    float width() { return width; }
}