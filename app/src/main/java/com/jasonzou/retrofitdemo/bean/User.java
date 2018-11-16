package com.jasonzou.retrofitdemo.bean;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {
    //一定使用Long,(使用long会出现无法存入数据的问题)
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String password;
    public int age;

    @Generated(hash = 45767339)
    public User(Long id, String account, String password, int age) {
        this.id = id;
        this.account = account;
        this.password = password;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 在此处返回默认值
     *
     * @return
     */
    public String getPassword() {
        if (TextUtils.isEmpty(this.password))
            return "默认值";
        else
            return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
