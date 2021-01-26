package com.hqyj.controller;

import com.hqyj.pojo.Info;
import com.hqyj.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

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
}
