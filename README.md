# README
保存学习笔记以及相关代码，使用 arphan 分支分离不同项目/语言的代码，使用分支名(spring_code_demo ...)进行区分

# orphan 的使用

```
# 创建 orphan分支 (注意创建了分支必须提交文件到该分支下，否则其实没有创建成功)
git checkout --orphan 分支名

# 提交到 orphan 分支下，push 到远程仓库需要指定分支
git add .
git commit -m "desc"
git push origin 分支名（确保一致）

# 切换分支，比如切换到main分支（切换了文件会发生变化，只会显示该分支下的文件）
git checkout main
```