package com.hqyj.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hqyj.dao.UserInfoDao;
import com.hqyj.pojo.UserInfo;
import com.hqyj.util.MdFive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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


    //创建操作redis库的操作对象
    @Autowired
    RedisTemplate<String,Object> redisTemplate;


    /**
     * 检查用户是否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
     * @param username  username
     * @return  时间
     */
    private long getUserLoginTimeLock(String username) {
        String key = "user:" + username + ":lockTime";
        //获取key 过期时间
        long lockTime = redisTemplate.getExpire(key, TimeUnit.SECONDS);

        if(lockTime > 0){//查询用户是否已经被锁定，如果是，返回剩余锁定时间，如果否，返回-1
            return lockTime;
        }else{
            return -1;
        }
    }




    /**
     * 设置失败次数
     * @param username  username
     */
    private void setFailCount(String username){
        //获取当前用户已失败次数
        long count = this.getUserFailCount(username);
        String key = "user:" + username + ":failCount";
        if(count < 0){//判断redis中是否有该用户的失败登陆次数，如果没有，设置为1，过期时间为1分钟，如果有，则次数+1
            redisTemplate.opsForValue().set(key,1,60,TimeUnit.SECONDS);
        }else{
            redisTemplate.opsForValue().increment(key,new Double(1));
        }
    }



    /**
     * 获取当前用户已失败次数
     * @param username  username
     * @return  已失败次数
     */
    private int getUserFailCount(String username){
        String key = "user:" + username + ":failCount";
        //从redis中获取当前用户已失败次数
        Object object = redisTemplate.opsForValue().get(key);
        if(object != null){
            return (int)object;
        }else{
            return -1;
        }
    }





    //登录处理方法

    @Override
    public String login(UserInfo user,HttpServletRequest request) {

        //先判断用户是否被所定,如果剩余时间大于0，就是锁定状态
        if (this.getUserLoginTimeLock(user.getUserName())<0){


            //查询用户名是否存在，如果存在则取出他的盐值
            //user是前端传来的数据，userFromSql是从数据库中查询的数据
            //如果查询到则返回那一条数据，如果没有查询到则会返回空值
            UserInfo userFromSql = userInfoDao.selectByName(user);

            if (userFromSql!=null){

                //加密用户输入的密码，第二个参数是该用户的盐值
                String pwd = mdFive.encrypt(user.getUserPwd(), userFromSql.getSalt());

                //把加过密的密码传到数据层中
                user.setUserPwd(pwd);

                //查询数据库层的登陆方法，验证用户名和加密后的密码是否一样，并且拿到返回值
                UserInfo userInfo = userInfoDao.login(user);

                //如果用户名和密码一样，userinfo就不等不null，否则就等于null
                if(userInfo!=null){

                    //创建session对象
                    //从session中获取当前用户信息
                    HttpSession session = request.getSession();
                    //存入用户对象
                    session.setAttribute("user",userInfo);
                    return "登录成功";
                }
                else {
                    //密码输入错误
                    //设置密码输入失败次数
                    setFailCount(user.getUserName());

                    //获取密码失败次数
                    int userFailCount = getUserFailCount(user.getUserName());

                    //定义用户允许失败而次数
                    int cc=3;

                    //剩余失败次数等于cc-userFailCount
                    int d=cc-userFailCount;
                    if (d>0){
                        return "你输入密码错误"+userFailCount+"次，还剩余"+d+"次";
                    }
                    else {
                        String key = "user:" + user.getUserName() + ":lockTime";
                        //设置失效时间60秒后
                        redisTemplate.opsForValue().set(key,222,60,TimeUnit.SECONDS);
                        return "你输入的密码超过"+cc+"次，用户账号被锁定";
                    }

                }
            }
            else {
                return "用户名输入错误";
            }
        }

        else {
            return "用户账号已被锁定，请一分钟后再尝试";
        }

    }



    //注册
    @Override
    public String register(UserInfo user) {

        //查询用户名是否重名
        int num = userInfoDao.valName(user);
        //如果重名则被注册
        if (num>0){
            return "用户名已经被注册";
        }else {
            //自动生成盐值,以后最好写一个不重复的算法
            Random rd = new Random();
            String salt = String.valueOf(rd.nextInt(10000));

            //加密用户输入的密码
            String pwd = mdFive.encrypt(user.getUserPwd(), salt);

            //把加过密的密码传到数据层中
            user.setUserPwd(pwd);
            //存入盐值
            user.setSalt(salt);

            //开始注册
            int n = userInfoDao.register(user);
            //如果插入条数大于0则注册成功
            if (n>0){
                //更新缓存
                String key = "wan";
                //查询mysql数据
                List<UserInfo> list = userInfoDao.select();
                //存入到缓存中
                redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);
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
        //1 设置分页参数,参数：页码，页码显示条数
        PageHelper.startPage(user.getPage(),user.getRow());


        //2 根据用户选择的查询条件判断用户需要查询
        List<UserInfo> list = new ArrayList<>();

        //判断用户输入的查询条件是否有值
        if ("".equals(user.getConValue())){

            //判断redis缓存中是否有数据
            String key = "wen";
            Object obj = redisTemplate.opsForValue().get(key);
            if (obj==null){
                //查询mysql数据
                list = userInfoDao.select();
                //存入到缓存中
                redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);
            }
            else {
                //取缓存数据
                list=(List<UserInfo>) obj;
            }

        }
        else {

            if ("编号".equals(user.getCondition())){

                //设置用户输入的查询条件
                user.setUserId(Integer.parseInt(user.getConValue()));
                list = userInfoDao.findByUserId(user);
            }
            else if("用户名".equals(user.getCondition())){

                //设置用户输入的查询条件
                user.setUserName(user.getConValue());
                list = userInfoDao.findByUserName(user);
            }
            else {
                list = userInfoDao.select();
            }
        }



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

    @Override
    public UserInfo selectByUserId(UserInfo user) {

        return userInfoDao.selectByUserId(user);
    }

    @Override
    public String update(UserInfo user) {

        //验证修改用户名是否重名
        int v = userInfoDao.valName(user);
        if (v==0){
            int num = userInfoDao.update(user);
            if (num>0){
                //更新缓存
                String key = "wan";
                //查询mysql数据
                List<UserInfo> list = userInfoDao.select();
                //存入到缓存中
                redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);
                return "修改成功";
            }
            return "修改失败";
        }
        else {
            return "用户名重名";
        }
    }

    @Override
    public String del(UserInfo user) {
        int num = userInfoDao.del(user);
        if (num>0){
            //更新缓存
            String key = "wan";
            //查询mysql数据
            List<UserInfo> list = userInfoDao.select();
            //存入到缓存中
            redisTemplate.opsForValue().set(key,list,10,TimeUnit.MINUTES);
            return "删除成功";
        }
        return "删除失败";
    }


    @Override
    public String updatePwd(UserInfo user,HttpServletRequest request) {
        //取出用户存入session的密码
        HttpSession session = request.getSession();
        UserInfo realUser = (UserInfo) session.getAttribute("user");
        String pwd = realUser.getUserPwd();
        //加密旧密码
        String oldPwd = mdFive.encrypt(user.getOldPwd(),realUser.getSalt());


        //判断用户输入的旧密码是否正确
        if (oldPwd.equals(pwd)){

            //加密新密码
            String p = mdFive.encrypt(user.getNewPwd(),realUser.getSalt());
            //存入新密码
            realUser.setUserPwd(p);
            int num=userInfoDao.updatePwd(realUser);
            if (num>0){
                return "修改密码成功";
            }

        }
        else {
            return "旧密码输入不正确";
        }

        return "修改密码失败";
    }

    @Override
    public String updateHead(UserInfo user, HttpServletRequest request) {
        //从session中获取当前用户信息
        HttpSession session = request.getSession();
        UserInfo u = (UserInfo) session.getAttribute("user");
        //存入要修改的用户id
        user.setUserId(u.getUserId());
        int num = userInfoDao.updateHead(user);
        if (num>0){
            return "保存成功";
        }
        return "保存失败";
    }
}
