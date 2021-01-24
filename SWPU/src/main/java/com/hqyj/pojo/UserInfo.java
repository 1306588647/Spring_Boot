package com.hqyj.pojo;


//用来描述表userInfo的信息
public class UserInfo  extends MyPage{

    //描述userInfo中userId列的
    private int userId;

    //描述userInfo中userName列的
    private String userName;

    //描述userInfo中userPwd列的
    private String userPwd;

    //描述salt的列
    private String salt;

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
