package com.hqyj.dao;

import com.hqyj.pojo.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

//关于userInfo表的数据库操作
//具体怎么查的，交给框架去做，不必关心

@Mapper     //注解表示这个接口是执行mybatis的数据库操作
public interface UserInfoDao {

    //查询用户名是否存在,用于验证登录查询盐值
    @Select("select * from userInfo where userName=#{userName}")
    UserInfo selectByName(UserInfo user);


    //登录
    //注解会根据sql语句查询，条件语句中前面是列名，后面的是从下面方法参数中的对象中的属性取出来的值，也就是传进来的值
    //之所以只需要填写一个对象是因为，列名和属性名是相同的
    @Select("select * from userInfo where userName=#{userName} and userPwd=#{userPwd}")
    UserInfo login(UserInfo user);




    //验证用户名是否重名,返回值为查询结果的个数
    @Select("select count(*) from userInfo where userName=#{userName}")
    int valName(UserInfo user);


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

    //删除
    @Delete("delete from userInfo where userId=#{userId}")
    int del(UserInfo user);

    //根据编号查询
    @Select("select * from userInfo where userId=#{userId} ")
    List<UserInfo> findByUserId(UserInfo user);

    //根据用户名查询
    @Select("select * from userInfo where userName=#{userName} ")
    List<UserInfo> findByUserName(UserInfo user);

    //修改密码
    @Update("update userInfo set userPwd=#{userPwd} where userId=#{userId}")
    int updatePwd(UserInfo user);

    //修改头像
    @Update("update userInfo set url=#{url} where userId=#{userId}")
    int updateHead(UserInfo user);

}
