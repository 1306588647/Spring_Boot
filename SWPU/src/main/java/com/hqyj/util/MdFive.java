package com.hqyj.util;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

/**
 * 密码加密的工具类
 */

@Component//代表该对象交给Spring去管理
public class MdFive {
    /**
     *
     * @param password 要加密的密码
     * @param saltValue 盐值，一般是用户名为盐值，比如用户名密码都是一样的，
     *                  加密后密码还是一样的，使用盐值后，密码和盐值关联，虽然密码一样，
     *                  但是用户名不一样，因此加密后还是不一样
     * @return
     */
    public String encrypt(String password,String saltValue){

        Object salt = new Md5Hash(saltValue);

        Object result = new SimpleHash("MD5", password, salt, 1024);

        return result+"";
    }



}

