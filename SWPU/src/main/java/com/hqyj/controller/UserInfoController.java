package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/user")
public class UserInfoController {

    @Autowired
    UserInfoService userInfoService;



    //访问user-list页面
    @RequestMapping("/list")
    public String list(UserInfo user, ModelMap m) {
        //查询分页数据
        HashMap<String, Object> map = userInfoService.select(user);
        //把数据传到前端
        m.put("info",map);

        return "user-list";
    }
}
