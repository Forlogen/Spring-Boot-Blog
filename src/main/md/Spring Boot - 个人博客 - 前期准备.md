[TOC]

------

> 开篇名义，在此感谢李仁密老师的[**《Spring Boot开发小而美的个人博客》**](https://www.bilibili.com/video/BV1KJ411R7XL?p=1)课程，本系列文章是对做该项目过程中的一种记录，也是个人学习的一些心得体会。希望通过本课程不仅可以学会搭建个性化的个人博客，也能将之前学习的东西整合使用，从而达到融会贯通的目的~

___

### 1. 功能概览

**小而美**的个人博客所提供的功能大致如下图所示：

![个人博客脑图](https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/个人博客脑图.jpg)

功能整体上分为两大部分：

- 管理员
  - 登录
  - 博客CRUD
  - 类别CRUD
  - 标签CRUD
- 前端展示
  - 博客首页
  - 博客分类
  - 博客标签
  - 博客归档
  - 导航栏、底部

### 2. 技术选型

- 前端：HTML、CSS、JS、[**Thymleaf**](https://www.thymeleaf.org/)、[**Semantic UI**](https://semantic-ui.com/)

- 后端：[**Spring Boot**](https://spring.io/projects/spring-boot)、[**Spring Data Jpa**](https://spring.io/projects/spring-data-jpa)

- 数据库：MySQL

- 工具：IDEA、JDK8、Maven3

- 插件：

  - [编辑器 Markdown](https://pandao.github.io/editor.md/)

  - [内容排版 typo.css](https://github.com/sofish/typo.css)

  - [动画 animate.css](https://daneden.github.io/animate.css/)

  - [代码高亮 prism](https://github.com/PrismJS/prism)

  - [目录生成 Tocbot](https://tscanlin.github.io/tocbot/)

  - [滚动侦测 waypoints](http://imakewebthings.com/waypoints/)

  - [平滑滚动 jquery.scrollTo](https://github.com/flesler/jquery.scrollTo)
  - [二维码生成 qrcode.js](https://davidshimjs.github.io/qrcodejs/)

  

### 3. 环境搭建

#### 3.1 依赖导入

首先在IDEA中创建Spring Boot项目，并导入项目所需的依赖项，例如Thymleaf、mysql-connector、Jpa、DevTools、Aspects、Lomlok……完整的pom.xml如下所示：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.1.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>dyliang</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dyliang</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <thymeleaf.version>3.0.11.RELEASE</thymeleaf.version>
        <thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.thymeleaf/thymeleaf -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-aop -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
            <version>2.3.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
            <version>2.3.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.3.6.Final</version>
        </dependency>
         <!--实现MakeDown转HTML-->
        <dependency>
            <groupId>com.atlassian.commonmark</groupId>
            <artifactId>commonmark</artifactId>
            <version>0.10.0</version>
        </dependency>
        <dependency>
            <groupId>com.atlassian.commonmark</groupId>
            <artifactId>commonmark-ext-heading-anchor</artifactId>
            <version>0.10.0</version>
        </dependency>
        <dependency>
            <groupId>com.atlassian.commonmark</groupId>
            <artifactId>commonmark-ext-gfm-tables</artifactId>
            <version>0.10.0</version>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

#### 3.2 配置文件

这里配置文件采用yaml格式，当然properties格式的配置文件同样可以。首先编写全局的配置文件application.yaml

```yaml	
spring:
  thymeleaf:
    mode: HTML
  # 运行环境
  profiles:
    active: dev
    # 国际化
    messages:
      basename: i18n/messages
```

为了适配于不同的应用环境，创建application-dev.yaml和application-pro.yaml分别用于开发环境和生产环境

```yaml 
########################
# application-dev.yaml
########################
spring:
  # 配置数据源
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/blog?serverTimezone=GMT&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
  # Jpa相关配置项
  jpa:
    hibernate:
      # 根据实体类自动创建表
      ddl-auto: update
    # 自动打印sql语句
    show-sql: true

# 日志配置
logging:
  # 日志级别
  level:
    root: info
    com.lrm: debug
 # 日志文件目录
 file:
    name: log/blog-dev.log
```

```yaml
########################
# application-pro.yaml
########################
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/blog?serverTimezone=GMT&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true

logging:
  level:
    root: warn
    com.lrm: info
  file:
    name: log/blog-pro.log
server:
  port: 8081
```

同时也可以添加Spring Boot默认的日志logback的相关配置，编写配置文件logback-spring.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<configuration>
    <!--包含Spring boot对logback日志的默认配置-->
    <include resource="org/springframework/boot/logging/logback/defaults.xml" />
    <property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
    <include resource="org/springframework/boot/logging/logback/console-appender.xml" />

    <!--重写了Spring Boot框架 org/springframework/boot/logging/logback/file-appender.xml 配置-->
    <appender name="TIME_FILE"
              class="ch.qos.logback.core.rolling.RollingFileAppender">
        <encoder>
            <pattern>${FILE_LOG_PATTERN}</pattern>
        </encoder>
        <file>${LOG_FILE}</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_FILE}.%d{yyyy-MM-dd}.%i</fileNamePattern>
            <!--保留历史日志一个月的时间-->
            <maxHistory>30</maxHistory>
            <!--
            Spring Boot默认情况下，日志文件10M时，会切分日志文件,这样设置日志文件会在100M时切分日志
            -->
            <timeBasedFileNamingAndTriggeringPolicy class="ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP">
                <maxFileSize>10MB</maxFileSize>
            </timeBasedFileNamingAndTriggeringPolicy>

        </rollingPolicy>
    </appender>

    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="TIME_FILE" />
    </root>

</configuration>
```

#### 3.3 异常处理

首先，定义常用的错误页面404、500和error，并编写全局的异常处理类ControllerExceptionHandler：

```java
@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView ExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Requst URL : {}，Exception : {}", request.getRequestURL(),e);

        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception", e);
        mv.setViewName("error/error");
        return mv;
    }
}
```

并定义资源找不到异常类NotFoundExcepiton

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException() {}

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

#### 3.4 日志处理

这里使用Spring Boot中的AOP进行日志管理，编写记录日志类LogAspect

```java
@Aspect  // 表示该类作为一个切面
@Component  // 将其加入到Ioc容器中
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // @pointcut中指定要扫描的内容对应的AspectJ表达式
    @Pointcut("execution(* dyliang.controller.*.*(..))")
    public void log() {}


    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}", requestLog);
    }

    @After("log()")
    public void doAfter() {}

    @AfterReturning(returning = "result",pointcut = "log()")
    public void doAfterReturn(Object result) {
        logger.info("Result : {}", result);
    }

    @AllArgsConstructor
    @ToString
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

    }
}
```

### 4. 实体类设计

这里通过Jpa来使用面向对象的思想根据实体类自动的创建表，所以并不需要在数据库中显式的创建表，并设置表之间的映射关系。首先分析一下博客系统所设计的实体类：

- 博客类：Blog
- 类别类：Type
- 标签类：Tag
- 评论类：Comment
- 用户类：User

它们之间的关系如下所示：

<img src="https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/Blog类图.png" alt="Blog类图" style="zoom:67%;" />

除了不同实体类之间的关系，由于博客评论这里设计为两级结构，因此父评论和下面的子评论还存在一对多的关系。

**User**

```java
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    private Integer type;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user")
    private List<Blog> blogs = new ArrayList<>();

}
```

**Blog**

```java
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_blog")
public class Blog {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    @Getter
    @Setter
    private String firstPicture;

    @Getter
    @Setter
    private String flag;

    @Getter
    @Setter
    private Integer views;

    @Getter
    @Setter
    private Boolean appreciation;

    @Getter
    @Setter
    private Boolean shareStatement;

    @Getter
    @Setter
    private Boolean commentable;

    @Getter
    @Setter
    private Boolean published;

    @Getter
    @Setter
    private Boolean recommend;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    // 博客对应的多个标签，该部分数据并不写入数据库
    @Getter
    @Setter
    @Transient
    private String tagIds;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    @ManyToOne
    private Type type;

    @Getter
    @Setter
    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    public void init() {
        this.tagIds = tagsToIds(this.getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        if (!tags.isEmpty()) {
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return tagIds;
        }
    }
}
```

**Comment**

```java
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nickname;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Getter
    @Setter
    @ManyToOne
    private Blog blog;

    @Getter
    @Setter
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> replyComments = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne
    private Comment parentComment;

    @Getter
    @Setter
    private boolean adminComment;
}

```

**Tag**

```java
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_tag")
public class Tag {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "tags")
    private List<Blog> blogs = new ArrayList<>();
}
```

**Type**

```java
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "t_type")
public class Type {

    @Getter
    @Setter
    @Id
    @GeneratedValue
    private Long id;

    @Getter
    @Setter
    @NotBlank(message = "分类名称不能为空")
    private String name;

    @Getter
    @Setter
    @OneToMany(mappedBy = "type")
    private List<Blog> blogs = new ArrayList<>();
}
```

### 5. 命名约定

**Service/DAO层命名约定：**

*  获取单个对象的方法用get做前缀。
*  获取多个对象的方法用list做前缀。
*  获取统计值的方法用count做前缀。
*  插入的方法用save(推荐)或insert做前缀。
*  删除的方法用remove(推荐)或delete做前缀。
*  修改的方法用update做前缀。

### 6. 应用分层

<img src="https://typora-forlogen.oss-cn-shenzhen.aliyuncs.com/img/应用分层.png" alt="应用分层" style="zoom:67%;" />