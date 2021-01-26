package com.hqyj.service;

import com.hqyj.pojo.Info;

import java.util.HashMap;
import java.util.Map;

public interface InfoService {

    //饼图
    HashMap<String, Object> bing(Info info);
}
