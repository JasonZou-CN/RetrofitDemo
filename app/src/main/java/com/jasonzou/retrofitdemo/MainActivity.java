package com.jasonzou.retrofitdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonzou.retrofitdemo.bean.UserInfo;
import com.jasonzou.retrofitdemo.network.API;
import com.jasonzou.retrofitdemo.network.APIMaster;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private TextView who;
    private EditText account;
    private android.widget.Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.account = (EditText) findViewById(R.id.account);
        this.button = (Button) findViewById(R.id.button);
        this.who = (TextView) findViewById(R.id.who);
    }

    public void login(View view) {
        String phone = account.getText().toString();
        phone = phone.isEmpty() ? "13219155257" : phone;


        API api = APIMaster.getAPI();
        useRxjavaCallAdapter(phone, api);

    }

    /**
     * RXJava2 + Retrofit
     *
     * @param phone
     * @param api
     */
    private void useRxjavaCallAdapter(String phone, API api) {
        Observable<UserInfo> userInfo = api.loginWithRxJava(phone, "123456", "account");
        userInfo.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UserInfo userInfo) {
                Toast.makeText(MainActivity.this, "更新", Toast.LENGTH_SHORT).show();
                who.setText(userInfo.data.list.realname);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * Retrofit
     *
     * @param phone
     * @param api
     */
    private void useNormalCallAdapter(String phone, API api) {
        Call<UserInfo> userInfo = api.login(phone, "123456", "account");
        userInfo.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                who.setText(response.body().data.list.realname);
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "exception", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
