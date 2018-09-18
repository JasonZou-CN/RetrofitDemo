package com.jasonzou.retrofitdemo.network;

import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * antuor:邹旭
 * 文件上传，进度可监听,基于HttpURLConnection
 * Post请求；
 */
public class FileUploader {

    private final String end = "\r\n";
    private final String twoHyphens = "--";
    private final String boundary = "*****";

    boolean interupted = false; // 是否强制停止下载线程
    private final String urlStr; // 下载URL
    private IOnFileUpdateListener listener;
    private final Map<String, String> parms;
    private final File uploadFile;

    private int progress;//当前进度
    private int total;//已上传数据大小
    private long max;

    private FileUploader(Builder builder) {
        urlStr = builder.urlStr;
        listener = builder.listener;
        parms = builder.parms;
        uploadFile = builder.uploadFile;

        if (urlStr == null || urlStr.isEmpty())
            throw new RuntimeException("PARM urlStr must not null or empty");
        if (parms == null)
            Logger.d("PARM parms is null");
        if (uploadFile == null || !uploadFile.isFile())
            throw new RuntimeException("PARM uploadFile must not null and must be a File");
    }

    public static Builder newBuilder(String urlStr, Map<String, String> parms, File uploadFile) {
        return new Builder(urlStr, parms, uploadFile);
    }

    public FileUploader upload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    /*【1.请求头部】*/
                    /* 允许Input、Output，不使用Cache */
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    con.setUseCaches(false);
                    /* 设置传送的method=POST */
                    con.setRequestMethod("POST");
                    /* setRequestProperty */
                    con.setRequestProperty("Connection", "Keep-Alive");
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                    /* 设置DataOutputStream */
                    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                    /*【2.基本参数】*/
                    for (String key : parms.keySet()) {
                        dos.writeBytes(twoHyphens + boundary + end);
                        dos.writeBytes("Content-Disposition: form-data; " + "name=\"" + key + "\"" + end);//参数格式配置
                        dos.writeBytes(end);
                        dos.writeBytes(parms.get(key) + end);//参数值
                    }

                    /*【3.文件参数】*/
                    dos.writeBytes(twoHyphens + boundary + end);
                    dos.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + uploadFile.getName() + "\"" + end);
                    dos.writeBytes(end);
                    /* 取得文件的FileInputStream */
                    FileInputStream fis = new FileInputStream(uploadFile);
                    max = fis.getChannel().size();
                    if (listener != null) {
                        //listener.onStart(max);
                    }
                    /* 设置每次写入1024bytes */
                    int bufferSize = 1024;
                    byte[] buffer = new byte[bufferSize];
                    int length;
                    /* 从文件读取数据至缓冲区 */
                    while (!interupted && (length = fis.read(buffer)) != -1) {//取消上传控制
                        /* 将资料写入DataOutputStream中 */
                        total += length;
                        dos.write(buffer, 0, length);
                        if (listener != null) {
                            progress = (int) Math.rint(total * 1.0 / max * 100f);
                            listener.onProgress(progress);//百分比-整数
                        }
                    }
                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    /* close streams */
                    fis.close();
                    dos.flush();


                    /*【4.取得Response内容】*/
                    InputStream is = con.getInputStream();
                    int ch;
                    StringBuffer b = new StringBuffer();
                    while ((ch = is.read()) != -1) {
                        b.append((char) ch);
                    }
                    if (is != null && listener != null) {
                        listener.onSuccess(b.toString().trim());
                    }
                    /* 将Response显示于Dialog */
                    Logger.d("上传成功:" + b.toString().trim() );
                    /* 关闭DataOutputStream */
                    dos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null)
                        listener.onFail(e);
                    Logger.d("FileUploader", "上传失败:" + e);
                }
            }
        }).start();
        return this;
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 强行终止文件下载
     */
    public FileUploader cancel() {
        interupted = true;
        return this;
    }

    /**
     * Created with Android Studio
     * User:邹旭
     * Date:2017/7/21
     * Time:16:25
     * Desc:下载&上传过程监听
     */
    public interface IOnFileUpdateListener {
        void onSuccess(String response);

        void onProgress(int progress);

        void onFail(Exception e);

    }

    public static final class Builder {
        private final String urlStr;
        private IOnFileUpdateListener listener;
        private final Map<String, String> parms;
        private final File uploadFile;

        private Builder(@NonNull String urlStr, @NonNull Map<String, String> parms,@NonNull File uploadFile) {
            this.urlStr = urlStr;
            this.parms = parms;
            this.uploadFile = uploadFile;
        }

        public Builder listener(IOnFileUpdateListener val) {
            listener = val;
            return this;
        }

        public FileUploader build() {
            return new FileUploader(this);
        }
    }
}
