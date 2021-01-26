package com.hqyj.service;

import com.hqyj.dao.InfoDao;
import com.hqyj.pojo.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Service
public class InfoServiceImpl implements InfoService{

    @Autowired
    InfoDao infoDao;


    @Override
    public HashMap<String, Object> bing(Info info) {
        HashMap<String,Object> map = new HashMap<>();
        //查询数据库
        List<Info> list = infoDao.select(info);

        //构建饼图需要的数据类型
        List<HashMap<String,Object>> mapList = new ArrayList<>();
        //遍历查询的数据集合
        for (Info i : list) {
            HashMap<String,Object> m =new HashMap<>();
            m.put("value",i.getCuredCount());
            m.put("name",i.getProvinceName());
            //添加到构建饼图的即合理
            mapList.add(m);
        }
        //把构建好的饼图数据存到map中
        map.put("info",mapList);
        return map;
    }
}
