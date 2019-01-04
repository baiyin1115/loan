# 小贷登记系统
 
## 前言

 - 小贷登记系统是在[loan](https://github.com/stylefeng/loan)的基础上将数据库层由mybatis替换为spring data jpa的系统。
 - 小贷登记系统是一个基于spring boot的后台管理系统。

### 分支
- admin-flat 该分支将loan-admin页面更改为扁平化风格，去掉iframe+标签页的形式:

 ![flat](https://user-images.githubusercontent.com/3115814/38806871-49f57248-41ad-11e8-932b-e06dc1941107.jpg)

- oracle, 该分支将底层数据使用oracle。


## 目录说明
- loan-admin 一个成熟的后台管理系统，完全具备了后台管理系统的基本功能
- loan-admin-vuejs 基于vuejs的后台管理,如果你想要前后端分离，那么该目录和下面的loan-api可以帮到你，这两个模块共同实现了上面loan-admin实现了的功能
- loan-api 基于vuejs后台管理的api服务
- loan-utils 工具包
- loan-dao  dao层
- loan-entity 实体层
- loan-business 服务层

## 技术选型

- 核心框架：spring boot
- 数据库层：spring data jpa
- 安全框架：Shiro
- 数据库连接池：Druid
- 缓存：Ehcache
- 前端：Beetl模版+Bootstrap

## 使用

- 克隆本项目
- 导入idea或者eclipse
- 创建数据库：loan，将db/sql/loan.sql导入数据库中，更改相应数据库配置
- 启动loan-admin
- 访问 https://localhost:8080，   登录，用户名密码:admin/admin
- ![demo](docs/md/demo.gif)
- 另外附上vue版本效果图![vue](docs/md/vuejs.gif)

## 文档



## 感谢

- [stylefeng](https://github.com/stylefeng) 没有他就没有小贷登记系统

## 交流


