package com.hqyj.controller;

import com.hqyj.pojo.Info;
import com.hqyj.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/info")
public class InfoController {

    @Autowired
    InfoService infoService;

    //访问饼图页面
    @RequestMapping("/bing")
    public String bing() {
        return "info/bing";
    }

    //处理饼图的ajax的请求
    @RequestMapping("/bingAjax")
    @ResponseBody
    public HashMap<String,Object> bingAjax(Info info){
        return infoService.bing(info);
    }


    //查询时间
    @RequestMapping("/time")
    @ResponseBody
    public List<Info> time(){
        return infoService.selectTime();
    }


    //访问柱状图页面
    @RequestMapping("/zhu")
    public String zhu() {
        return "info/zhu";
    }

    //处理柱状图的ajax的请求
    @RequestMapping("/zhuAjax")
    @ResponseBody
    public HashMap<String,Object> zhuAjax(Info info){
        return infoService.zhu(info);
    }



    //访问地图页面
    @RequestMapping("/china")
    public String china() {
        return "info/china";
    }


    //访问世界地图页面
    @RequestMapping("/world")
    public String world() {
        return "info/world";
    }

}
