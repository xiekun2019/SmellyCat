## 流浪猫狗救助信息系统
本系统可以发布流浪猫狗的一些信息，也可以求助养猫、养狗知识，发布"寻猫启示" 等等。

## 简介
开源流浪猫狗论坛、集成问答系统，现有功能多社交平台登录(Github，Gitee)，提问、回复、通知、最新问答、最热帖子、消除零回复等功能。


## 使用技术
1. Spring boot
2. Spring MVC

## 实现功能


## 数据库脚本
```sql
create table USER
(
    ID           INT auto_increment
        primary key,
    ACCOUNT_ID   VARCHAR(100),
    NAME         VARCHAR(50),
    TOKEN        CHAR(36),
    GMT_CREATE   BIGINT,
    GMT_MODIFIED BIGINT
);
```
未完待续。。。
