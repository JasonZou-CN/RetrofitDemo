package com.jasonzou.retrofitdemo.ui.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.bean.Profile;
import com.jasonzou.retrofitdemo.bean.User;
import com.jasonzou.retrofitdemo.greendao.DaoSession;
import com.jasonzou.retrofitdemo.greendao.DbMaster;
import com.jasonzou.retrofitdemo.greendao.ProfileDao;
import com.jasonzou.retrofitdemo.greendao.UserDao;

import java.util.Date;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
    }

    public void insert(View view) {
        /*User user = new User();
        user.setAccount(String.valueOf(System.currentTimeMillis()));
        UserDao dao = DbMaster.session().getUserDao();
        dao.insert(user);*/

        Profile profile = new Profile();
        profile.setBirthday(new Date());
        profile.setIsBoy(true);
        ProfileDao pdao = DbMaster.session().getProfileDao();
        pdao.save(profile);

        User user = null;
        UserDao uDao = DbMaster.session().getUserDao();
        for (int i = 0; i < 40; i++) {
            user = new User();
            user.age = (int) (Math.random() * 30);
            uDao.save(user);
        }

        profile.setUser(user);
        pdao.save(profile);

    }

    public void queryBuilder(View view) {

        DaoSession session;
        session = DbMaster.session();

        //        //  ------------简单查询&条件查询
        //        List<User> users;
        //        QueryBuilder<User> qb = session.queryBuilder(User.class);
        //        qb.whereOr(UserDao.Properties.Age.eq(25), qb.and(UserDao.Properties.Age.gt(7), UserDao.Properties.Age.le(12)));
        //        users = qb.list();
        //        //Logger.json(new Gson().toJson(session.loadAll(User.class)));
        //        Logger.json(new Gson().toJson(users));

        //        //  ------------查询SQL复用查询器Query<T>&条件查询,【修改查询条件参数】->Query
        //        QueryBuilder<User> qb = session.queryBuilder(User.class);
        //        qb.whereOr(UserDao.Properties.Age.eq(25), qb.and(UserDao.Properties.Age.gt(7), UserDao.Properties.Age.le(12)));
        //        Query<User> query = qb.build();
        //        Logger.json(new Gson().toJson(query.list()));
        //        query.setParameter(0, 0);
        //        query.setParameter(1, Integer.MAX_VALUE);
        //        query.setParameter(2, Integer.MAX_VALUE);
        //        Logger.json(new Gson().toJson(query.list()));
        //        //query.forCurrentThread()//多线程执行，避免setParameter()相互干扰,重置查询参数为QueryBuilder中设置的参数


        //        //  ------------条件删除->DeleteQuery
        //        QueryBuilder<User> qb = session.queryBuilder(User.class);
        //        qb.whereOr(UserDao.Properties.Age.eq(25), qb.and(UserDao.Properties.Age.gt(7), UserDao.Properties.Age.le(12)));
        //        Query<User> query = qb.build();
        //        long id = query.list().get(0).getId();
        //        Logger.json(new Gson().toJson(query.list()));
        //        DeleteQuery<User> delQ = qb.buildDelete();
        //        delQ.executeDeleteWithoutDetachingEntities();//删除对象，缓存中的可以在后面相同session中复活，但是数据库中的记录，已经删除
        //        Logger.json(new Gson().toJson(query.list()));
        //        Logger.d("复活---%s",session.load(User.class,id));//此时的是Session中缓存的数据


        //query.forCurrentThread()//多线程执行，避免setParameter()相互干扰,重置查询参数为QueryBuilder中设置的参数


        //        //  ------------懒加载->listLazy()
        //        QueryBuilder<User> qb = session.queryBuilder(User.class);
        //        qb.whereOr(UserDao.Properties.Age.eq(25), qb.and(UserDao.Properties.Age.gt(7), UserDao.Properties.Age.le(12)));
        //        LazyList<User> users = qb.listLazy();
        //        Logger.d("已加载:" + users.getLoadedCount());//输出:0
        //        if (users.size() > 0) {
        //            users.get(1);
        //        }
        //        Logger.d("已加载:" + users.getLoadedCount());//输出:1


        //        //  -------------分页查询->limit(),offset()
        //        QueryBuilder<User> qb = session.queryBuilder(User.class);
        //        Query<User> query = qb.limit(1).offset(1).build();
        //        for (int i = 0; i < 3; i++) {
        //            query.setLimit(2);
        //            query.setOffset(i * 2);
        //            List<User> page = query.list();
        //            Logger.json(new Gson().toJson(page));
        //        }
    }

    public void deleteAll(View view) {
        DaoSession session = DbMaster.session();
        session.deleteAll(User.class);
        session.deleteAll(Profile.class);
    }
}
