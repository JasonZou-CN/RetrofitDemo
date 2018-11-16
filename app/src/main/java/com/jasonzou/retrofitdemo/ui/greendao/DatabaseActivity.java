package com.jasonzou.retrofitdemo.ui.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.bean.User;
import com.jasonzou.retrofitdemo.greendao.GreenDaoMaster;
import com.jasonzou.retrofitdemo.greendao.UserDao;
import com.orhanobut.logger.Logger;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
    }

    public void insert(View view) {
        User user = new User();
        user.setAccount(String.valueOf(System.currentTimeMillis()));
        UserDao dao = GreenDaoMaster.getDaoSession().getUserDao();
        dao.insert(user);
        Toast.makeText(this, "new ID:" + user.getId(), Toast.LENGTH_SHORT).show();
        Logger.d("dao.count()="+dao.count());

    }

    public void QueryBuilder(View view) {
    }
}
