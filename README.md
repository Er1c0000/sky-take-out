# 苍穹外卖

## 项目简介
苍穹外卖是一个基于 Java 和 Spring Boot 开发的外卖系统，提供了员工管理、菜品管理、套餐管理、订单管理、微信支付等功能，适用于各类外卖业务场景。

## 环境要求
- **JDK**：1.8 及以上
- **Maven**：3.6 及以上
- **MySQL**：8.0 及以上
- **微信开发者工具**：用于测试微信支付功能

## 项目结构
- `sky-common`：通用工具类和常量定义
- `sky-pojo`：实体类、DTO 和 VO 类定义
- `sky-server`：项目主服务，包含控制器、服务层和配置类

## 安装步骤

### 1. 克隆项目
```bash
git clone https://github.com/your-repo/sky-take-out.git
cd sky-take-out
```

### 2. 配置数据库
打开 `sky-take-out/sky-server/src/main/resources/application-dev.yml` 文件，配置数据库连接信息：
```yaml
sky:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    host: localhost
    port: 3306
    database: sky_take_out
    username: root
    password: root
```

### 3. 配置 JWT
打开 `sky-take-out/sky-server/src/main/resources/application.yml` 文件，配置 JWT 相关信息：
```yaml
sky:
  jwt:
    # 设置jwt签名加密时使用的秘钥
    admin-secret-key: itcast
    # 设置jwt过期时间
    admin-ttl: 7200000
    # 设置前端传递过来的令牌名称
    admin-token-name: token
```

### 4. 配置微信支付
打开 `sky-common/src/main/java/com/sky/properties/WeChatProperties.java` 文件，配置微信支付相关信息：
```java
@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Data
public class WeChatProperties {
    private String appid; //小程序的appid
    private String secret; //小程序的秘钥
    private String mchid; //商户号
    private String mchSerialNo; //商户API证书的证书序列号
    private String privateKeyFilePath; //商户私钥文件
    private String apiV3Key; //证书解密的密钥
    private String weChatPayCertFilePath; //平台证书
    private String notifyUrl; //支付成功的回调地址
    private String refundNotifyUrl; //退款成功的回调地址
}
```

### 5. 构建项目
使用 Maven 构建项目：
```bash
mvn clean install
```

### 6. 启动项目
运行 `sky-take-out/sky-server/src/main/java/com/sky/SkyApplication.java` 类的 `main` 方法启动项目。

## 功能特性
- **员工管理**：员工登录、密码修改、员工信息管理
- **菜品管理**：菜品的增删改查、口味管理
- **套餐管理**：套餐的增删改查、套餐与菜品关联
- **订单管理**：订单提交、订单支付、订单退款、订单状态管理
- **微信支付**：支持小程序支付和退款功能

## 接口文档
项目使用 Knife4j 生成接口文档，启动项目后，访问 `http://localhost:8080/doc.html` 即可查看详细的接口信息。

## 注意事项
- 请确保数据库服务和微信支付相关配置正确，否则可能导致部分功能无法正常使用。
- 项目中使用了 JWT 进行身份验证，请妥善保管 JWT 秘钥，避免泄露。

本文档由豆包生成:)
