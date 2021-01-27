package com.hqyj.controller;

import com.hqyj.pojo.UserInfo;
import com.hqyj.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Controller
public class IndexController {

    @Autowired
    UserInfoService userInfoService;

    //访问index页面
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    //访问welcome页面
    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }

    //访问修改密码页面updatePwd
    @RequestMapping("/updatePwd")
    public String updatePwd() {
        return "updatePwd";
    }

    //处理修改密码的ajax请求
    @RequestMapping("/updatePwdAjax")
    @ResponseBody
    public HashMap<String, Object> updatePwdAjax(UserInfo user, HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        String info = userInfoService.updatePwd(user, request);
        map.put("info", info);
        return map;
    }


}
