package com.hqyj.controller;


import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller //该注解表示与前端交互的类
@RequestMapping("/login")
public class LoginController {

    //创建一个userInfoService的实现类对象,由框架容器来做
    @Autowired
    UserInfoService userInfoService;


    @RequestMapping("/loginPage")
    //定义一个登录访问login.html的方法
    public String loginPage(){
        return "login";
    }


    //ajax登录
    @RequestMapping("/loginAjax")
    @ResponseBody   //此时返回时，不在返回页面，而返回数据，数据格式为刚刚定义的的json格式
    public HashMap<String,Object> loginAjax(UserInfo user,HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<>();
        String info = userInfoService.login(user,request);
        map.put("info",info);
        return map;
    }



    //访问注册页面
    @RequestMapping("/registerPage")
    //再定义一个登录访问register.html的方法
    public String registerPage(){
        return "register";
    }


    //处理注册
    @RequestMapping("/register")
    @ResponseBody
    public HashMap<String,Object> registerAjax(UserInfo user){
        HashMap<String,Object> map = new HashMap<>();

        //访问注册方法
        String info = userInfoService.register(user);
        map.put("info",info);
        return map;
    }


    //处理邮件发送的请求
    @RequestMapping("/sendEmail")
    @ResponseBody
    public HashMap<String,Object> sendEmail(String email, HttpServletRequest request){
        return userInfoService.sendCode(email,request);
    }


    //处理邮件登录的请求
    @RequestMapping("/emailLogin")
    @ResponseBody
    //request是用来获取session对象，因为session是全局性的
    public HashMap<String,Object> emailLogin (String code,HttpServletRequest request){
        HashMap<String,Object> map = new HashMap<>();
        //判断用户输入的验证码和邮箱接受的验证码是否一致
        //获取session对象
        HttpSession session = request.getSession();
        //取出session中的验证码
        //根据键值来获取邮箱的验证码
        String valCode = (String) session.getAttribute("valCode");
        if(code.equals(valCode)){
            map.put("info","邮箱登录成功");
        }else {
            map.put("info","验证码输入错误");
        }
        return map;
    }
}
