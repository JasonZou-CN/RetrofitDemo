package com.jasonzou.retrofitdemo.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.greendao.DbMaster<br>
 * 描述:  略<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/28 17:53<br>
 */
public class DbMaster {
    private static DaoMaster mDaoMaster;
    private static boolean hasInited = false;

    /**
     * 在Application中初始化
     * 设置greenDao
     */
    public static void init(Context context) {
        if (hasInited) {
            Logger.w("init(Context) has call in Application !");
        } else {
            DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "lawyer-db", null);
            SQLiteDatabase db = mHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            logSQL();
            hasInited = true;
        }
    }

    /**
     * 打印sql语句:在QueryBuilder.build()时打印
     */
    private static void logSQL() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public static DaoSession session() {
        if (hasInited)
            return mDaoMaster.newSession();//Session中会缓存数据
        throw new RuntimeException("init(Context) must call in Application firstly!");
    }
}
