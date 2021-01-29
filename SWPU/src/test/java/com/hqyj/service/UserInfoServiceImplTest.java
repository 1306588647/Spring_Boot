package com.hqyj.service;

import com.hqyj.pojo.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;



@RunWith(SpringRunner.class)
@SpringBootTest
public class UserInfoServiceImplTest {

    //创建操作redis库的操作对象
    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    //redis的数据设置和提取
    @Test
    public void add(){
        //对应mysql中的插入
        //设置值
        //set(键名，值，时间长度，单位)
        redisTemplate.opsForValue().set("name","张三",30, TimeUnit.SECONDS);

        //获取失效时间
        long time = redisTemplate.getExpire("name");
        System.out.println("time="+time);

        //获取键名的剩余失效时间
        long mytime = redisTemplate.getExpire("name",TimeUnit.SECONDS);
        System.out.println("指定的失效时间="+mytime);

        //对应mysql中的查询
        //取值
        String name = String.valueOf(redisTemplate.opsForValue().get("name"));
        System.out.println("name"+name);


        //插入一个userInfo对象到redis中
        UserInfo user = new UserInfo();
        user.setUserId(1);
        user.setUserName("rock");
        user.setUserPwd("123");


        //插入操作
        redisTemplate.opsForValue().set("user",user);

        //取值
        UserInfo user1 = (UserInfo) redisTemplate.opsForValue().get("user");
        System.out.println(user1.toString());


    }
}