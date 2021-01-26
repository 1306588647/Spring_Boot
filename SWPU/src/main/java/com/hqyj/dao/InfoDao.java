package com.hqyj.dao;

import com.hqyj.pojo.Info;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InfoDao {

    //查询34个省的新冠疫情数据
    @Select("select * from info where time=#{time} and areaName is null")
    List<Info> select(Info info);
}
