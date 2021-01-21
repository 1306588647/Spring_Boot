package com.hqyj;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  //加上这注解以后，代表这个类是启动服务器的类
public class MyApplication {

    //定义一个启动方法
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class,args);
    }
}
