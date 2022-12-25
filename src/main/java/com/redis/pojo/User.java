package com.redis.pojo;

import java.io.Serializable;

/**
 * @author ldq
 * @version 1.0
 * @date 2022/12/25 20:27
 * @Description: 用户实体类
 */
public class User implements Serializable {

    private Integer uid;
    private String uname;
    private String upwd;

    public User(int uid, String uname, String upwd) {
        this.uid = uid;
        this.uname = uname;
        this.upwd = upwd;
    }

    public User(){}

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpwd() {
        return upwd;
    }

    public void setUpwd(String upwd) {
        this.upwd = upwd;
    }



    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", uname='" + uname + '\'' +
                ", upwd='" + upwd + '\'' +
                '}';
    }
}
