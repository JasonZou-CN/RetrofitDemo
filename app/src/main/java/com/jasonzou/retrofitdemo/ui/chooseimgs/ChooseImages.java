package com.jasonzou.retrofitdemo.ui.chooseimgs;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jasonzou.retrofitdemo.R;
import com.jasonzou.retrofitdemo.eventbus.message.MessageEvent;
import com.jasonzou.retrofitdemo.util.ImageUriResolver;
import com.jasonzou.retrofitdemo.util.MPermissions;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class ChooseImages extends AppCompatActivity {

    private MPermissions writeFileP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_images);
    }

    public void chosesMultiPics(View view) {
        writeFileP = MPermissions.newBuilder(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}).requestCode(1).iCalllback(new MPermissions.ICalllback() {
            @Override
            public void onAllGranted() {
                MultiImageSelector.create().showCamera(true).count(2).single().multi().start(ChooseImages.this, 1);
            }

            @Override
            public void onSomeDenied(String[] avoids) {

            }
        }).build().request();
    }

    public void chosePicture(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (writeFileP != null && requestCode == writeFileP.getRequestCode()) {
            writeFileP.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK) {
            if (requestCode==0) {
                String path = ImageUriResolver.handleImageUri(data.getData(), this);
                Logger.d(path);
                EventBus.getDefault().post(new MessageEvent(path));
            }else if (requestCode==1){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Logger.d(path);
            }
        }
    }
}
