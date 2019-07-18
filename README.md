# jiliguala-newbie

Clojure 新人项目

[![Build Status](https://travis-ci.com/uncleshushu/jiliguala-newbie.svg?branch=shawn-impl)](https://travis-ci.com/uncleshushu/jiliguala-newbie)
[![codecov](https://codecov.io/gh/uncleshushu/jiliguala-newbie/branch/shawn-impl/graph/badge.svg)](https://codecov.io/gh/uncleshushu/jiliguala-newbie)

## TODO

- [ ] （可选）完成[基础语法的所有练习](https://github.com/functional-koans/clojure-koans) （[在线版](http://clojurescriptkoans.com)）

- [x] 基于[compojure-api + ring](https://github.com/metosin/compojure-api)搭建一个HTTP Rest Server，
提供接口输出Hello World，使用Postman进行测试

- [x] 接入log4j2，为线上/线下提供不同的配置文件

- [x] 提供一个接口，返回登录用户的微信信息，要求实现静默授权,可以直接使用微信公众平台测试号进行测试。(建议使用微信开发者工具在本地开发)

- [ ] 本地启动一个Mongodb，手动创建DB。准备一个edn配置文件，接入monger，在项目启动时读取配置文件并连接到Mongodb

- [ ] 在db中构造一个用户-订单关系，提供用户注册、注销、登录、下单等接口，使用Postman测试

- [x] 编写单测，接入travis ci 和 cloverage。生成单测报告，要求code coverage在50%以上

## Usage

### Run the application locally

`lein ring server`

### Packaging and running as standalone jar

```
lein do clean, ring uberjar
java -jar target/server.jar
```

### Packaging as war

`lein ring uberwar`
