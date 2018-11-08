package com.jasonzou.retrofitdemo.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.bean.CaseList;
import com.jasonzou.retrofitdemo.bean.CaseListParm;
import com.jasonzou.retrofitdemo.bean.IMConversationState;
import com.jasonzou.retrofitdemo.bean.UserInfo;
import com.jasonzou.retrofitdemo.eventbus.message.MessageEvent;
import com.jasonzou.retrofitdemo.greendao.GreenDaoMaster;
import com.jasonzou.retrofitdemo.network.API;
import com.jasonzou.retrofitdemo.network.APIMaster;
import com.jasonzou.retrofitdemo.network.FileDownloader;
import com.jasonzou.retrofitdemo.network.FileUploader;
import com.jasonzou.retrofitdemo.ui.chooseimgs.ChooseImages;
import com.jasonzou.retrofitdemo.ui.gifloading.GifLoadingActivity;
import com.jasonzou.retrofitdemo.util.MPermissions;
import com.jasonzou.retrofitdemo.util.MPopwindow;
import com.orhanobut.logger.Logger;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private TextView who;
    private EditText account;
    private android.widget.Button button;
    private MPermissions permiss;
    private MPermissions zxing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.account = (EditText) findViewById(R.id.account);
        this.button = (Button) findViewById(R.id.button);
        this.who = (TextView) findViewById(R.id.who);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permiss != null && requestCode == permiss.getRequestCode()) {
            permiss.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else if (zxing != null && requestCode == zxing.getRequestCode()) {
            zxing.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void login(View view) {
        String phone = account.getText().toString();
        phone = phone.isEmpty() ? "13219155257" : phone;


        API api = APIMaster.getAPI();
        //        getProjectsAfterLogin(phone, api);

        permiss = MPermissions.newBuilder(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}).iCalllback(new MPermissions.ICalllback() {
            @Override
            public void onAllGranted() {
                uploadFile();
            }

            @Override
            public void onSomeDenied(String[] avoids) {

            }
        }).requestCode(0).build().request();

    }

    /**
     * 上传文件
     */
    public void uploadFile() {
        String url = "https://lz.lcoce.com/lawyer/" + "upload/uploadAvatarFile";
        Map<String, String> param = new HashMap<>();
        param.put("uid", 194 + "");
        param.put("token", "bf30b2007a359cb17e6a694231d34c0a");
        param.put("type", "1");

        FileUploader.newBuilder(url, param, new File(Environment.getExternalStorageDirectory(), "temp.jpg")).listener(new FileUploader.IOnFileUpdateListener() {
            @Override
            public void onSuccess(String response) {
                who.setText(response);
            }

            @Override
            public void onProgress(int progress) {
                who.setText(String.format("%d%%", progress));
            }

            @Override
            public void onFail(Exception e) {
                who.setText(e.toString());
            }

            @Override
            public void onStart(long max) {
                who.setText(String.format("%d", max));
            }
        }).build().upload();
    }


    /**
     * 下载文件
     */
    private void downloadFile() {
        String url = "https://lvzhe-project-file.oss-cn-beijing.aliyuncs.com/project2060/5adda34e39d39.jpg";
        File fileSaved = new File(Environment.getExternalStorageDirectory(), url.replace("/", "_"));
        FileDownloader.newBuilder(url, fileSaved).listener(new FileDownloader.IOnFileDownloadListener() {
            @Override
            public void onSuccess(int id, File file) {
                who.setText(file.getAbsolutePath());
                Logger.i("下载完成");
            }

            @Override
            public void onStart(int id, int total) {
                who.setText(String.valueOf(total));
            }

            @Override
            public void onProgress(int id, int progress) {
                //                who.setText(progress+"%");
                who.setText(String.format("%d%%", progress));
            }

            @Override
            public void onFail(Integer obj, Exception e) {
                who.setText(e.toString());
            }
        }).build().download();

    }


    /**doOnNext(Consumer) + flatMap(Function)<br/>
     * 链式API请求,使用操作符，解决API嵌套调用
     *
     * @param phone
     * @param api
     */
    private void getProjectsAfterLogin(String phone, final API api) {
        api.loginWithRxJava(phone, "123456", "account").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnNext(new Consumer<UserInfo>() {
            @Override
            public void accept(UserInfo userInfo) throws Exception {
                who.setText(userInfo.data.list.realname);
                Logger.d("登录用户：" + userInfo.data.list.realname);
            }
        }).observeOn(Schedulers.io()).flatMap(new Function<UserInfo, ObservableSource<CaseList>>() {
            @Override
            public ObservableSource<CaseList> apply(UserInfo userInfo) throws Exception {
                CaseListParm parm = new CaseListParm();
                parm.uid = userInfo.data.list.uid;
                parm.token = userInfo.data.list.token;
                parm.category = 1;
                parm.typeId = 0;
                parm.pageSize = 1;
                parm.listRows = 2;
                parm.sort = 2;
                parm.keywords = "";
                return api.getProjectList(parm);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<CaseList>() {
            @Override
            public void accept(CaseList caseList) throws Exception {
                if (caseList.data.list == null || caseList.data.list.size() == 0) {
                    return;
                }
                who.setText(caseList.data.list.get(0).title);
                Logger.d("项目1：{名字：" + caseList.data.list.get(0).name + "}");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                Logger.d("exception:" + throwable.getMessage());
            }
        });
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

    /**Android M 动态权限
     * 调用ZXing的扫描二维码界面
     */
    private void toCaptureQRCode() {
        zxing = MPermissions.newBuilder(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}).iCalllback(new MPermissions.ICalllback() {
            @Override
            public void onAllGranted() {
                startActivity(new Intent(getBaseContext(), CaptureActivity.class));
            }

            @Override
            public void onSomeDenied(String[] avoids) {
                Logger.d("---权限onFail---");
            }
        }).build().request();
    }

    public void toZXingLib(View view) {
        toCaptureQRCode();
    }

    /**popwindow
     * @param view
     */
    public void showPop(View view) {
        MPopwindow popup = MPopwindow.newBuilder(this, R.layout.dialog_commom)
                .iOnDissmiss(new MPopwindow.IOnDissmiss() {
                    @Override
                    public boolean onIntercept() {
                        Toast.makeText(MainActivity.this, "dissmiss...", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                })
                .intercetpOutsideTouch(false)
                .mWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .mAnim(R.style.BottomPopupWindowTheme)
                .build();
        popup.showAtLocation(findViewById(Window.ID_ANDROID_CONTENT), Gravity.CENTER, 0, 0);
    }

    @Subscribe(threadMode= ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Logger.d(event.message);
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void toChooesePics(View view) {
        startActivity(new Intent(this, ChooseImages.class));
    }

    /**简单插入
     * @param view
     */
    public void greenDao(View view) {
        IMConversationState state = new IMConversationState();
        state.setTargetPhone(String.valueOf(new Date().getTime()));
        GreenDaoMaster.getDaoSession().getIMConversationStateDao().insert(state);
    }

    public void gifLoading(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), GifLoadingActivity.class));
    }
}
