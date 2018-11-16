package com.jasonzou.retrofitdemo.ui.greendao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.bean.IMConversationState;
import com.jasonzou.retrofitdemo.greendao.GreenDaoMaster;
import com.jasonzou.retrofitdemo.greendao.IMConversationStateDao;
import com.orhanobut.logger.Logger;

import java.util.Date;

public class DatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
    }

    public void insert(View view) {
        IMConversationState state = new IMConversationState();
        state.setTargetPhone(String.valueOf(new Date().getTime()));
        IMConversationStateDao dao = GreenDaoMaster.getDaoSession().getIMConversationStateDao();
        dao.insert(state);
        Toast.makeText(this, "state.getId():" + state.getId(), Toast.LENGTH_SHORT).show();
        Logger.d("dao.count()="+dao.count());

    }
}
