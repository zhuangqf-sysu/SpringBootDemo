# SpringBootDemo
搭建一个基于Spring Boot的Rest Web开发框架，同时
 - 1、改用undertow服务器；
 - 2、配置jdbc pool；
 - 3、利用Mybatis编写增删查改;
 - 4、利用jax-rs编写endpoint;

## 0、搭建Spring Boot
### （1）、create project from maven archetype：maven-archetype-quickstart
### （2）、添加spring boot依赖：

    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>com.example.zhuangqf</groupId>
      <artifactId>sever</artifactId>
      <packaging>war</packaging>
      <version>1.0-SNAPSHOT</version>
      <name>sever Maven Webapp</name>
      <url>http://maven.apache.org</url>
        <!--   Inherit    defaults   from   Spring Boot   -->
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>1.4.0.RELEASE</version>
        </parent>
        <!--   Add    typical    dependencies   for    a  web    application    -->
        <dependencies>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>3.8.1</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
        </dependencies>
        <!--   Package    as an executable jar    -->
        <build>
            <finalName>sever</finalName>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </project>

### (3) 在java目录下新建包，并编写一个Controller：HelloWorldController

    package com.example.zhuangqf.controller;

    import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @RestController
    @EnableAutoConfiguration
    public class HelloWorldCotroller {

        @RequestMapping("/")
        public String helloWorld(){
            return "hello world";
        }

    }

类上使用的第一个注解是 @RestController ,这被称为构造型(stereotype)注解。它为阅读代码的人提供暗示(这是一个支持REST的控制器),对于Spring,该类扮演了一个特殊角色。在本示例中,我们的类是一个web @Controller ,所以当web请求进来时,Spring会考虑是否使用它来处理。
@RequestMapping 注解提供路由信息,它告诉Spring任何来自"/"路径的HTTP请求都应该被映射到 home 方法。 @RestController 注解告诉Spring以字符串的形式渲染结果,并直接返回给调用者。
第二个类级别的注解是 @EnableAutoConfiguration ,这个注解告诉Spring Boot根据添加的jar依赖猜测你想如何配置Spring。由于 spring-boot-starter-web 添加了Tomcat和Spring MVC,所以auto-configuration将假定你正在开发一个web应用,并对Spring进行相应地设置。

### （4） 写一个App类用以启动spring boot server

    package com.example.zhuangqf;

    import com.example.zhuangqf.controller.HelloWorldCotroller;
    import org.springframework.boot.SpringApplication;

    public class App
    {
        public static void main( String[] args )
        {
            SpringApplication.run(HelloWorldCotroller.class);
        }
    }

我们的main方法通过调用 run ,将业务委托给了Spring Boot的SpringApplication类。SpringApplication将引导我们的应用,启动Spring,相应地启动被自动配置的Tomcat web服务器。运行main方法后访问localhost:8080,就可以看到网页上出现hello world(如果配置没出错的话)

## 1、改用undertow服务器
     Tomcat是spring boot的默认服务器，使用Undertow替代Tomcat，你需要排除Tomcat依赖,并包含Undertow starter。maven:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-undertow</artifactId>
    </dependency>

重新启动，可以看到日志消息：

    2016-11-18 19:20:41.404 INFO 12201 --- [ main] o.s.w.s.handler.SimpleUrlHandlerMapping : Mapped URL path [/**] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
    2016-11-18 19:20:41.474 INFO 12201 --- [ main] o.s.w.s.handler.SimpleUrlHandlerMapping : Mapped URL path [/**/favicon.ico] onto handler of type [class org.springframework.web.servlet.resource.ResourceHttpRequestHandler]
    2016-11-18 19:20:41.693 INFO 12201 --- [ main] o.s.j.e.a.AnnotationMBeanExporter : Registering beans for JMX exposure on startup
    2016-11-18 19:20:41.792 INFO 12201 --- [ main] b.c.e.u.UndertowEmbeddedServletContainer : Undertow started on port(s) 8080 (http)
    2016-11-18 19:20:41.803 INFO 12201 --- [ main] com.example.zhuangqf.App : Started App in 4.829 seconds (JVM running for 5.478)

## 2、配置jdbc pool
### （1）pom：
    <!-- MYSQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    <!-- Spring Boot JDBC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>

### （2）src下新建resources目录（如果没有的话），添加application.properties:

    spring.datasource.url=jdbc:mysql://localhost:3306/test
    spring.datasource.username=root
    spring.datasource.password=123456
    spring.datasource.driver-class-name=com.mysql.jdbc.Driver

    spring.datasource.max-idle=10
    spring.datasource.max-wait=10000
    spring.datasource.min-idle=5
    spring.datasource.initial-size=5
    spring.datasource.max-active = 100
    spring.datasource.validation-query=SELECT 1
    spring.datasource.test-on-borrow=false
    spring.datasource.test-while-idle=true
    spring.datasource.time-between-eviction-runs-millis=18800
    spring.datasource.jdbc-interceptors=ConnectionState;SlowQueryReport(threshold=0)

### (3)编写Entity：Student：

    package com.example.zhuangqf.entity;

    import java.io.Serializable;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    public class Student implements Serializable{

        private int id;
        private String name;
        private int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

### （4）编写service：StudentService（暂用JdbcTample做持久层开发）：

    package com.example.zhuangqf.service;

    import com.example.zhuangqf.entity.Student;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jdbc.core.JdbcTemplate;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.stereotype.Service;

    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.List;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @Service
    public class StudentService {

        @Autowired
        private JdbcTemplate jdbcTemplate;

        public List<Student> getAll(){
            String sql  = "select id,name,age from student";
            return jdbcTemplate.query(sql,new StudentMapper());
        }

        class StudentMapper implements RowMapper<Student> {

            @Override
            public Student mapRow(ResultSet resultSet, int i) throws SQLException {
                Student student = new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setAge(resultSet.getInt("age"));
                return student;
            }
        }
    }

### （5）单元测试，pom：

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
    </dependency>

StudentServiceTest:

    package com.example.zhuangqf.service;

    import com.example.zhuangqf.App;
    import com.example.zhuangqf.entity.Student;
    import org.junit.Test;
    import org.junit.runner.RunWith;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.context.junit4.SpringRunner;

    import java.util.List;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @RunWith(SpringRunner.class)
    @SpringBootTest
    public class StudentServiceTest {

        @Autowired
        StudentService studentService;

        @Test
        public void getAll(){
            List<Student> studentList = studentService.getAll();
            for(Student student:studentList){
                System.out.println(student.getId()+" "+student.getName()+" "+student.getAge());
            }
        }

    }

结果：

    2 zhuangqf 18

## 3、利用Mybatis编写增删查改;
### (1) pom:

    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>1.1.1</version>
    </dependency>

### (2)编写Entity：Student(已实现)
### (3)编写Mapper：StudentMapper(这里采用注解配置，对多数据库的支持不好)

    package com.example.zhuangqf.mapper;

    import com.example.zhuangqf.entity.Student;
    import org.apache.ibatis.annotations.*;

    import java.util.List;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @Mapper
    public interface StudentMapper {

        @Select("select * from student")
        List<Student>findAllStudents();

        @Select("select * from student where id = #{id}")
        Student findById(@Param("id")int id);

        @Insert("insert student(name,age) values (#{name},#{age})")
        @Options(useGeneratedKeys = true, keyProperty = "id")
        int insert(Student student);

        @Update("update student set name=#{name},age=#{age} where id=#{id}")
        int update(Student student);

        @Delete("delete from student where id = #{id}")
        int deleteById(@Param("id")int id);

    }

### (4)App启动类 run

    package com.example.zhuangqf;

    import org.mybatis.spring.annotation.MapperScan;
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.boot.autoconfigure.domain.EntityScan;
    import org.springframework.boot.builder.SpringApplicationBuilder;
    import org.springframework.boot.web.support.SpringBootServletInitializer;

    @SpringBootApplication
    @EntityScan("com.example.zhuangqf.entity")
    @MapperScan("com.example.zhuangqf.mapper")
    public class App extends SpringBootServletInitializer {

        public static void main( String[] args ) {
            SpringApplication.run(App.class);
            new App().configure(new SpringApplicationBuilder(App.class)).run(args);
        }

    }

## 4、利用jax-rs编写endpoint
### (1)pom:

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jersey</artifactId>
    </dependency>

### (2)继承ResourceConfig 注册endpoint

    package com.example.zhuangqf.config;

    import com.example.zhuangqf.endpoint.StudentEndpoint;
    import org.glassfish.jersey.server.ResourceConfig;
    import org.springframework.stereotype.Component;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @Component
    public class JerseyConfig extends ResourceConfig {

        public JerseyConfig(){
            //register(StudentEndpoint.class);
            //注册包下的所有endpoint
            packages("com.example.zhuangqf.endpoint");
        }

    }

### (3) 编写Endpoint：StudentEndpoint

    package com.example.zhuangqf.endpoint;

    import com.example.zhuangqf.entity.Student;
    import com.example.zhuangqf.mapper.StudentMapper;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Component;

    import javax.ws.rs.*;
    import java.util.List;

    /**
     * Created by zhuangqf on 11/18/16.
     */
    @Component
    @Path("/student")
    public class StudentEndpoint {

        @Autowired
        StudentMapper studentMapper;

        @GET
        @Path("/all.json")
        @Produces("application/json")
        public List<Student>findAllStudents(){
            List<Student> studentList = studentMapper.findAllStudents();
            return studentList;
        }

        @GET
        @Path("/{id}.json")
        @Produces("application/json")
        public Student findById(@PathParam("id") int id){
            Student student = studentMapper.findById(id);
            return student;
        }

        @POST
        @Path("/insert.json")
        @Consumes("application/json")
        @Produces("application/json")
        public Student insert(Student student){
            studentMapper.insert(student);
            return student;
        }

        @POST
        @Path("/update.json")
        @Consumes("application/json")
        @Produces("application/json")
        public Student update(Student student){
            studentMapper.update(student);
            return student;
        }

        @DELETE
        @Path("/delete/{id}")
        public String delete(@PathParam("id")int id){
            int result = studentMapper.deleteById(id);
            if(result==1) return "delete success";
            return "delete failed";
        }

    }

### (4)修改App启动类

      package com.example.zhuangqf;

      import org.mybatis.spring.annotation.MapperScan;
      import org.springframework.boot.autoconfigure.SpringBootApplication;
      import org.springframework.boot.autoconfigure.domain.EntityScan;
      import org.springframework.boot.builder.SpringApplicationBuilder;
      import org.springframework.boot.web.support.SpringBootServletInitializer;

      @SpringBootApplication
      @EntityScan("com.example.zhuangqf.entity")
      @MapperScan("com.example.zhuangqf.mapper")
      public class App extends SpringBootServletInitializer {

          public static void main( String[] args ) {
              new App().configure(new SpringApplicationBuilder(App.class)).run(args);
          }

      }

### (5) postman测试，不贴图了

## 参考：
 - 数据库配置 http://blog.csdn.net/catoop/article/details/50507516
 - Spring Boot官方范例 https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples
 - mybatis范例 https://github.com/mybatis/spring-boot-starter
 - mybatis-spring-boot http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/
 - jersey 中文文档 https://github.com/waylau/Jersey-2.x-User-Guide
 - Spring Boot参考指南https://www.gitbook.com/book/qbgbook/spring-boot-reference-guide-zh/discussions
