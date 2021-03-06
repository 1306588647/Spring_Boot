package com.hqyj.pojo;


import java.io.Serializable;

//用来描述表userInfo的信息
public class UserInfo extends MyPage implements Serializable {

    //描述userInfo中userId列的
    private int userId;

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPwd='" + userPwd + '\'' +
                ", salt='" + salt + '\'' +
                ", condition='" + condition + '\'' +
                ", conValue='" + conValue + '\'' +
                ", oldPwd='" + oldPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    //描述userInfo中userName列的
    private String userName;

    //描述userInfo中userPwd列的
    private String userPwd;

    //描述salt的列
    private String salt;

    //描述查询条件condition的参数
    private String condition;

    //描述查询条件conValue的参数
    private String conValue;

    //描述修改密码旧密码
    private String oldPwd;

    //描述修改密码新密码
    private String newPwd;

    //描述url列
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getConValue() {
        return conValue;
    }

    public void setConValue(String conValue) {
        this.conValue = conValue;
    }



    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }
}
