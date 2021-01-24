package com.hqyj.dao;

import com.hqyj.pojo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

//关于userInfo表的数据库操作
//具体怎么查的，交给框架去做，不必关心

@Mapper     //注解表示这个接口是执行mybatis的数据库操作
public interface UserInfoDao {

    //登录
    //这个注解会把下面方法括号里面对象里面的属性值取出来放在大括号里面,并且把查询结果进行返回
    //前提是列名和属性值相同
    @Select("select * from userInfo where userName=#{userName} and userPwd=#{userPwd}")
    UserInfo login(UserInfo user);


    //验证用户名是否重名,返回值为查询结果的个数
    @Select("select count(*) from userInfo where userName=#{userName}")
    int valName(UserInfo user);


    //查询用户名是否存在，并且取出盐值
    @Select("select * from userInfo where userName=#{userName}")
    UserInfo seletByName(UserInfo user);



    //注册：返回值为插入结果成功的条数
    @Insert("insert into userInfo (userName,userPwd,salt) value (#{userName},#{userPwd},#{salt})")
    int register(UserInfo user);


    //查询
    @Select("select * from userInfo")
    List<UserInfo> select();


    //根据userId查询
    @Select("select * from userInfo where userId=#{userId} ")
    UserInfo selectByUserId(UserInfo user);

    //修改
    @Update("update userInfo set userName=#{userName} where userId=#{userId}")
    int update(UserInfo user);




}
