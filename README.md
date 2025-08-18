# README
electron study code demo

## 介绍

官网: https://www.electronjs.org/

+ 跨平台 桌面端 开发框架
+ electron = chromium + node.js + native api
+ 使用 html, css, js 等 web 技术开发

## electron 流程模型

主进程: 可以调用 native api，与操作系统进行交互

渲染进程

IPC: 主进程 <-- 调用 --> 渲染进程


## 初始化 

author 和 description 对于打包是必填项

```json
{
  "author": "xiaoen",
  "description": "demo"
}
```