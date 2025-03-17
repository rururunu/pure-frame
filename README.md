# 👀 PURE FRAME

## 简单的Spring Boot + Vue 3 开发框架

* WBE Server 引入了 MyBatis Flex 框架，感谢 [mybatis-flex](https://github.com/mybatis-flex/mybatis-flex)

* WBE 基于 pure-admin 进行开发，感谢 [pure-admin](https://github.com/pure-admin/vue-pure-admin)

#### start

* web 目录为 基于 [pure-admin](https://github.com/pure-admin/vue-pure-admin) 开发的 vue3 前端项目详情可查看 [官方文档](https://pure-admin.cn/)

* web_server 目录为 spring boot 框架的单体项目
* sql 目录为 项目数据库的构建 sql

##### database start

将 sql 目录中的 .sql 文件创建为 mysql 数据库

##### web server start

###### 环境

* 您的系统上至少需要拥有一个版本的 JDK，推荐 JDK 17

* 拉取下项目后，需要先进行 构建 等如下操作 才能使用  [mybatis-flex](https://github.com/mybatis-flex/mybatis-flex) 进行开发

  ![build_project](.\document\resource\build_project.png)

  ![mark_directory_as_generated_source_code](.\document\resource\mark_directory_as_generated_source_code.png)

* 推荐下载插件 [mybatis-flex-helper](https://plugins.jetbrains.com/plugin/22165-mybatis-flex-helper)

##### web start

* Node 版本，我使用的是 20.9.0 推荐使用 Node 发布的最新 LTS 版本 [nodejs](https://nodejs.org/en/download)

* 使用 pnpm 

  * ```shell
    npm install -g pnpm@latest-10
    ```

  * 访问 [安装 | pnpm](https://pnpm.io/zh/installation) 官网

* 启动项目

  * ```shell
    pnpm i
    ```

  * ```shell
    pnpm dev
    ```

* 登录帐号/密码
  * admin/admin123.

## 如果该项目对您有帮助请留下您宝贵的 start 🫶
