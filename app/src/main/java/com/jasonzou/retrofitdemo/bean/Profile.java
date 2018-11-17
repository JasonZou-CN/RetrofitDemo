package com.jasonzou.retrofitdemo.bean;

import android.text.TextUtils;

import com.jasonzou.retrofitdemo.greendao.DaoSession;
import com.jasonzou.retrofitdemo.greendao.ProfileDao;
import com.jasonzou.retrofitdemo.greendao.UserDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

@Entity
public class Profile {
    @Id
    private Long id;
    private String email;
    private String nickName;
    private Date birthday;
    private boolean isBoy;
    private Long uid;
    @ToOne(joinProperty = "uid")
    private User user;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 89320040)
    private transient ProfileDao myDao;


    @Generated(hash = 2119076645)
    public Profile(Long id, String email, String nickName, Date birthday,
            boolean isBoy, Long uid) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.birthday = birthday;
        this.isBoy = isBoy;
        this.uid = uid;
    }

    @Generated(hash = 782787822)
    public Profile() {
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;


    public String getEmail() {
        if (this.email == null || this.email.isEmpty()) {
            this.email = "***@gmail.com";
        }
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        if (TextUtils.isEmpty(this.nickName)) {
            this.nickName = "默认值";
        }
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getBirthday() {
        if (this.birthday==null) {
            this.birthday = new Date();
        }
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean getIsBoy() {
        return this.isBoy;
    }

    public void setIsBoy(boolean isBoy) {
        this.isBoy = isBoy;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return this.uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1390050865)
    public User getUser() {
        Long __key = this.uid;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 515172768)
    public void setUser(User user) {
        synchronized (this) {
            this.user = user;
            uid = user == null ? null : user.getId();
            user__resolvedKey = uid;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1351849779)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProfileDao() : null;
    }

}
