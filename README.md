# mxsm-log4j
优雅日志记录插件

### 如何使用

引入maven依赖

```xml
<dependency>
  <groupId>com.github.mxsm</groupId>
  <artifactId>mxsm-log</artifactId>
  <version>1.0.0</version>
</dependency>
```

在配置类或者Spring Boot的启动类上加入@EnableMxsmLog注解

```java
@SpringBootApplication
@EnableMxsmLog
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
```

或者

```java
@EnableMxsmLog
@Configuration
public class Config {

}
```

### 使用样例

在方法上使用@MxsmLog注解

```java
@Component
public class Test {

    public void test(){
        System.out.println(1111);
    }

    @MxsmLog(template = "用户名称${#user.name}信息：${@test.getName(#user)}")
    public boolean addUser(User user){
        return  true;
    }

    public String getName(User user){
        return  user.getName();
    }
}
```

- 样例地址：https://github.com/mxsm/spring-sample/tree/master/spring-boot
