# springboot mybatis-plus 达梦数据库

# 准备

官方网站: https://www.dameng.com

下载地址: https://www.dameng.com/list_103.html

管理工具 sqlark: https://www.sqlark.com

SQL使用文档: https://eco.dameng.com/document/dm/zh-cn/sql-dev

# 数据库实例的创建

使用工具 console.exe 创建数据库实例，该工具在达梦数据库安装目录下的 "\tool\dbca.exe" 中

# springboot 整合

添加依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <version>3.5.4</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.5.4</version>
</dependency>

<!-- 达梦数据库驱动 -->
<dependency>
    <groupId>com.dameng</groupId>
    <artifactId>DmJdbcDriver18</artifactId>
    <version>8.1.1.193</version>
</dependency>

<!-- MyBatis-Plus 依赖 -->
<!-- https://blog.csdn.net/u013737132/article/details/134938131 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.5</version>
</dependency>

<!-- lombok 依赖-->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

配置文件
```yml
server:
  port: 9696

spring:
  datasource:
    username: SYSDBA
    password: SYSDBA
    driver-class-name: dm.jdbc.driver.DmDriver
    url: jdbc:dm://127.0.0.1:5237?schema=DEMO1 # 指定模式名
```

创建 controller， service， mapper， entity 层代码，这里和正常的 springboot 项目一样。

需要注意的是创建数据库实例时，如果选择了大小写敏感，查询的时候表名需要加双引号，如果字段名不是大写，也需要加双引号








