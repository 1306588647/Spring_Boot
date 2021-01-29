package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller

public class WelcomeController {


    //创建操作redis库的操作对象
    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    //加载文章的赞数
    @RequestMapping("loadZan")
    @ResponseBody
    public HashMap<String,Object> loadZan(){
        HashMap<String,Object> map = new HashMap<>();
        //文章键名
        String wenKey = "arc";
        //获取赞数
        Object obj = redisTemplate.opsForValue().get(wenKey);
        if (obj==null){
            map.put("info",0);
        }
        else {
            map.put("info",obj);
        }

        return map;
    }


    @RequestMapping("zan")
    @ResponseBody
    public HashMap<String,Object> zan(HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<>();

        //获取用户名
        HttpSession session = request.getSession();
        UserInfo user = (UserInfo) session.getAttribute("user");
        //用户键名
        String key = user.getUserName()+"wen";

        //文章键名,用来记录赞数
        String wenKey = "arc";

        //先判断用户是否点过赞
        Object obj = redisTemplate.opsForValue().get(key);
        if (obj==null){
            //设置用户点赞
            redisTemplate.opsForValue().set(key,22);
            //给文章赞数加一
            redisTemplate.opsForValue().increment(wenKey,1);
        }
        else{//取消赞
            //文章赞数减少一   i--
            redisTemplate.opsForValue().decrement(wenKey,1);
            //删除用户点赞
            redisTemplate.delete(key);
        }

        //获取赞数
        int num = (int) redisTemplate.opsForValue().get(wenKey);
        map.put("info",num);
        return map;
    }
}
