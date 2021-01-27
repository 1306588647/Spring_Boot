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

            //判断用户统计的
            if (info.getCon().equals("0")){
                //加载确诊人数
                m.put("value",i.getConfirmCount());
            }
            else if (info.getCon().equals("1")){
                m.put("value",i.getCuredCount());
            }
            else{
                m.put("value",i.getDeadCount());
            }

            m.put("name",i.getProvinceName());
            //添加到构建饼图的即合理
            mapList.add(m);
        }
        //把构建好的饼图数据存到map中
        map.put("info",mapList);
        return map;
    }



    @Override
    public List<Info> selectTime() {
        return infoDao.selectTime();
    }

    @Override
    public HashMap<String, Object> zhu(Info info) {
        HashMap<String,Object> map = new HashMap<>();
        //查询数据库
        List<Info> list = infoDao.select(info);

        //构建X轴数据，省份数据
        List<String> xList= new ArrayList<>();

        //构建Y轴数据，人数数据
        List<Integer> yList= new ArrayList<>();
        //构建柱状图数据
        //构建人数y轴数据
        for (Info i : list) {
            if (info.getCon().equals("0")){
                //加载确诊人数
                yList.add(i.getConfirmCount());
            }
            else if (info.getCon().equals("1")){
                yList.add(i.getCuredCount());
            }
            else{
                yList.add(i.getDeadCount());
            }
            //构建省份x轴数据
            xList.add(i.getProvinceName());
        }

        //把x轴和y轴数据传到map中
        map.put("x",xList);
        map.put("y",yList);

        return map;
    }
}
