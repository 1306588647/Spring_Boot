package com.hqyj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.dao.UserInfoDao;
import com.hqyj.pojo.UserInfo;
import com.hqyj.util.MdFive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service    //该注解表示这个类的对象创建交给Spring去管理也就是加到容器里
public class UserInfoServiceImpl implements UserInfoService{

    //创建一个userInfoDao的实现类对象
    @Autowired
    UserInfoDao userInfoDao;

    //创建加密工具类对象
    @Autowired
    MdFive mdFive;


    //获取发件人邮箱,自动去配置文件获取邮箱值
    @Value("${spring.mail.username}")
    String sendEmail;


    //创建发送邮件的对象
    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public String login(UserInfo user,HttpServletRequest request) {

        //加密用户输入的密码
        String pwd = mdFive.encrypt(user.getUserPwd(), user.getUserName());

        //把加过密的密码传到数据层中
        user.setUserPwd(pwd);

        //查询数据库层的登陆方法，并且拿到返回值
        UserInfo userInfo = userInfoDao.login(user);


        //如果查到的值，userinfo就不等不null，否则就等于null
        if(userInfo!=null){

            //创建session对象
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //存入用户对象
            session.setAttribute("user",userInfo.getUserName());
            return "登录成功";
        }
        return "登录失败";
    }

    @Override
    public String register(UserInfo user) {

        //查询用户名是否重名
        int num = userInfoDao.valName(user);
        //如果重名则被注册
        if (num>0){
            return "用户名已经被注册";
        }else {
            //加密用户输入的密码
            String pwd = mdFive.encrypt(user.getUserPwd(), user.getUserName());

            //把加过密的密码传到数据层中
            user.setUserPwd(pwd);

            //开始注册
            int n = userInfoDao.register(user);
            //如果插入条数大于0则注册成功
            if (n>0){
                return "注册成功";
            }
        }
        return "注册失败";
    }



    //发送邮箱
    @Override
    public HashMap<String, Object> sendCode(String email, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            //从session中获取当前用户信息
            HttpSession session = request.getSession();
            //创建普通邮件对象
            SimpleMailMessage message = new SimpleMailMessage();
            //设置发件人邮箱
            message.setFrom(sendEmail);
            //设置收件人邮箱
            message.setTo(email);
            //设置邮件主题
            message.setSubject("学员信息管理系统验证码");
            // 生成随机验证码
            Random rd = new Random();
            String valCode = rd.nextInt(9999)+"";
            //设置邮件正文
            message.setText("你的验证码是："+valCode);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            //把验证码存入session中
            session.setAttribute("valCode",valCode);
            map.put("info","发送成功");


            HttpSession session1 = request.getSession();
            //存入用户对象
            session1.setAttribute("user",email);


            return map;

        } catch (Exception e) {
            System.out.println("发送邮件时发生异常！");
            e.printStackTrace();
        }
        map.put("info","发送邮件异常");
        return map;
    }

    @Override
    public HashMap<String, Object> select(UserInfo user) {
        HashMap<String, Object> map = new HashMap<>();
        //1 设置分页参数
        PageHelper.startPage(user.getPage(),user.getRow());

        //2  查询数据库表
        List<UserInfo> list = userInfoDao.select();

        //3 把查询的数据转换成分页对象
        PageInfo<UserInfo> page = new PageInfo<>(list);

        //获取分页当前页的集合
        map.put("list",page.getList());
        //总条数
        map.put("total",page.getTotal());
        //总页数
        map.put("totalPage",page.getPages());
        //上一页
        if(page.getPrePage()==0){
            map.put("pre",1);
        }
        else {
            map.put("pre",page.getPrePage());
        }

        //下一页
        //保持在最后一页
        map.put("next",page.getNextPage());
        if(page.getNextPage()==0){
            map.put("next",page.getPages());
        }
        else {
            map.put("next",page.getNextPage());
        }

        //当前页
        map.put("cur",page.getPageNum());

        return map;
    }
}
