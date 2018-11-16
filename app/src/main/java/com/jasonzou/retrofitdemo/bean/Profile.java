package com.jasonzou.retrofitdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Profile {
    @Id(autoincrement = true)
    public long id;
    public String email;
    public String nickName;
    public Profile() {
    }
    @Generated(hash = 741404033)
    public Profile(long id, String email, String nickName) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNickName() {
        return this.nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
