package com.jasonzou.retrofitdemo.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class User {
    @Id(autoincrement = true)
    public Long id;
    public String account;
    public String pwd;

    public User() {
    }

    @Generated(hash = 1061540729)
    public User(Long id, String account, String pwd) {
        this.id = id;
        this.account = account;
        this.pwd = pwd;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        if (TextUtils.isEmpty(pwd))
            this.pwd = "未定义";
        else
            this.pwd = pwd;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
