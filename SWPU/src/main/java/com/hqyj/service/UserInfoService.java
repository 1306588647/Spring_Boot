package com.hqyj.service;

import com.hqyj.pojo.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public interface UserInfoService {

    //定义登录方法,之所以返回String，是因为要返回登录成功或者登录失败，
    //第一个参数是前端传进来的，里面包含用户名和密码，第二个参数用来获取session对象，将用户名存入session对象中显示在index页面上的
    String login(UserInfo user,HttpServletRequest request);

    //注册，返回内容为注册成功或者失败等信息
    String register(UserInfo user);


    //定义一个邮件发送代码
    //第一个参数给谁发送
    HashMap<String,Object> sendCode(String toEmail, HttpServletRequest request);


    //查询
    HashMap<String,Object> select(UserInfo user);

    //根据UserId查询
    UserInfo selectByUserId(UserInfo user);

    //修改
    String update(UserInfo user);

    //删除
    String del(UserInfo user);

    //修改密码,需要用户名，所以需要session
    String updatePwd(UserInfo user,HttpServletRequest request);

    //修改头像
    String updateHead(UserInfo user,HttpServletRequest request);

}
