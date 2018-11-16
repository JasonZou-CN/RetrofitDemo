package com.jasonzou.retrofitdemo.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;

/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.greendao.GreenDaoMaster<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/28 17:53<br>
 */
public class GreenDaoMaster {

    private static DaoSession mDaoSession;
    private static boolean hasInited = false;

    /**
     * 在Application中初始化
     * 设置greenDao
     */
    public static void init(Context context) {
        if (hasInited) {
            Logger.w("init(Context) has call in Application !");
        } else {
            DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "retrofit-demo-db", null);
            SQLiteDatabase db = mHelper.getWritableDatabase();
            DaoMaster mDaoMaster = new DaoMaster(db);
            // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
            mDaoSession = mDaoMaster.newSession();
            hasInited = true;
        }

    }

    public static DaoSession getDaoSession() {
        if (hasInited)
            return mDaoSession;
        throw new RuntimeException("init(Context) must call in Application firstly!");
    }
}
