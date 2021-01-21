package com.hqyj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    //定义一个访问index页面
    @RequestMapping("/index")
    public String index() {
        return "index";
    }


    //定义一个访问welcome页面
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
}
