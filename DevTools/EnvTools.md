# Java

jenv: https://github.com/jenv/jenv

```shell
# 查看帮助文档
jenv -help

# 列出所有已注册的 Java 环境
jenv list	

# 添加一个新的 Java 版本到 JEnv，可以通过给定的名称引用
jenv add <name> <path>	

# 从 JEnv 中移除指定的 Java 版本
jenv remove <name>	

# 全局应用指定的 Java 版本（对所有重启的 shell 和当前 shell 生效）
jenv change <name>	

# 在当前 shell 中局部应用指定的 Java 版本
jenv use <name>	

# 在当前文件夹及其子文件夹中设置指定的 Java 版本
jenv local <name>	

# 为 JAVA_HOME 中的可执行文件（如 javac）创建快捷方式
jenv link <executable>	

# 卸载 JEnv 并将系统恢复为指定的 Java 版本（可选择保留配置文件）
jenv uninstall <name>	
```

# Node

nvm