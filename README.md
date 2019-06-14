### Clojure 新人项目
- [ ] 基础语法：https://github.com/functional-koans/clojure-koans.git
- [ ] 基于[compojure + ring](https://github.com/weavejester/compojure)搭建一个HTTP Server，提供HTTP接口，输出Hello World，使用Postman进行测试
- [ ] 提供一个接口，返回登录用户的微信头像昵称，要求实现静默授权,可以直接使用微信公众平台测试号进行测试。(建议使用微信开发者工具在本地开发)
- [ ] 本地启动一个Mongodb，准备一个配置文件，在项目启动时读取配置文件并连接到Mongodb
- [ ] 在db中构造一个用户-订单关系，提供用户注册、注销、登录认证、下单、取消订单接口，使用Postman测试
- [ ] 编写单测，要求单测覆盖率在50%以上