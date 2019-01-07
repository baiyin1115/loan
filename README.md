# 小贷登记系统
 
## 前言

 - 小贷登记系统是一个基于spring boot的后台管理系统。

### 分支
 - 无


## 目录说明
- loan-admin 一个成熟的后台管理系统，完全具备了后台管理系统的基本功能
- loan-utils 工具包
- loan-dao  dao层
- loan-entity 实体层
- loan-service 服务层

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
- 创建数据库：loan，将db/sql/mysql.sql导入数据库中，更改相应数据库配置
- 启动loan-admin
- 访问 https://localhost:8080，   登录，用户名密码:admin/admin
- 效果图完成后在放

## 文档
- 无


## 感谢 - 我们都是站在巨人的肩膀上 感谢他们

- [enilu](https://github.com/enilu/guns-lite) 感谢他的框架源码
- [stylefeng](https://github.com/stylefeng/Guns) 感谢他的框架源码

## 交流

## 修改履历
- fork项目后，由于是一个后台系统，直接阉割了原来的前后台分离的那个逻辑，直接用他的admin项目进行重构工作。
- 升级了spring boot版本为1.5.4 升级了部分其他jar包版本
- 将配置文件有properties修改为yml文件
- 修改了druid的连接池配置信息，引入druid-spring-boot-starter jar包，原项目中 不知道是不是我配置的问题，监控出不来，修改后  现在只有session监控不能显示了
- 业务逻辑代码持续增加中


