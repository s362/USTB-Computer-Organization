# 说明

## 写在前面的话
这个是目前正在使用的老版本，新版本在master分支上，还没写完，所以可以当它不存在，想优化接着写也可以。   
这套代码是大三写的，当时刚学后端开发，spring框架编写是点一点摸索出来的，很多东西都不符合规范，比如REST风格等，也没有太考虑性能问题，也没有加日志功能，因此不要太局限于我的代码，可以大胆的改。

## 一、总体架构

后端主要分为三个部分，javaweb服务、python后台测评，还有数据库。

### 1.1 javaweb服务
javaweb主要采用springboot框架，响应前端请求，修改数据库，并且调用python脚本进行评测。  
springboot框架网上有很多教程，小学期应该学过spring和java知识，上手应该不难。需要什么需求网上都能查到。  
因为后台学生代码采用gitlab进行管理，所以调用了gitlab api接口。网上也有教程。

### 1.2 python后台评测
这个后台评测封装为了一个docker，这个和CG系统的docker基本相同。通过向docker输入学生id、学生要评测的题目，就可以返回学生分数、评测波形等。

### 1.3 数据库
因为这是老版本，数据库设计的相对简单，并且因为spring框架对数据库相关操作不太熟悉，没有使用比如外键等操作，还有日期等采用了java.sql.date，没有用java.util.date，导致日期没有时、分。  
后续需要完成教师端功能，数据库需要大改。

## 二、文件说明
### 2.1 aspect
- ControllerRequestAdvice 网上查的，加上这个可以获取前端请求，并打印，便于debug。

### 2.2 Config
配置信息文件
- ExceptionController 加上他，可以捕获全局异常。
- MysqlConfig 没有他的话，后台数据库自动建库中文会乱码。
- ResponseBean 后台应该有一套和前端交互的标准返回格式，当时没有意识到这个很重要，没有用上。

### 2.3 Controller
- ErrorController 全局异常路由，如果出现异常，则执行这个代码。
- LoginController 登录，验证密码，密码正确返回jwt
- StudentController 学生服务，包括获取题目、进行评测等。
- TeacherController 老师服务，目前主要是出题。查看学生题目、评分等功能还没写。

### 2.4 Model
实体类
#### 2.4.1 DataModel
数据库实体类，进行建表、与数据库连接。
- JudgeModel 这个和python评测结果是一一对应的。用于把python返回转换为java对象（这个代码放错位置了，应该在GitModel下）
- Question 作业表
- Score 分数表
- Task 题目表（起名的时候没多考虑，一个question有多个task，指一次作业有多道题目）
- User 用户表
#### 2.4.2 GitModel
普通的实体类
- GitFile 前端IDE需要的文件格式
- GitFolder 没具体用处
- Gitproject 题目类，一道题目含有多个文件GitFile和其他信息。
- QuestionAndTask 题目和作业对应关系类。
- TaskFile 老师上传的文件转换为的对象
- TaskModel 老师上传的题目转换为的对象
- TaskSore 题目分数。

### 2.4.3 JudgeResult
评测结果文件。和JudgeModel一样，但是现在好像用的是和JudgeModel一样。

### 2.2.4 Result
标准返回格式，和ResponseBean一样，只不过这里用的是Result，没有状态码等信息。

### 2.3 Repository
与数据库对应的持久层

### 2.4 Serviece
与数据库对应的服务层，里面都是各种数据库增删改查操作。

### 2.5 Shiro
登录保护，直接用的框架。用法自行百度。

### 2.6 Util
工具类
- Base64Convert base64加密解密类
- FileUtil 老师上传作业题目的文件服务类。
- GitProcess gitlabapi封装，提供gitlab获取工程、文件删改的工具类
- OSUtile 获取当前服务器操作系统
- ResultUtil 返回格式类。

## 三、调试方式
- mysql 主要通过navicat或者mysqlworkbench
- java 通过postman模拟发送请求
- gitlab 老师上传的题目，id保存在数据库中，题目内容比如描述和上传的文件都保存在了gitlab里。gitlab访问（202.204.62.155:8099 root qq872940851）
