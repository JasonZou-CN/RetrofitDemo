package com.jasonzou.retrofitdemo.network;

import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 文件下载列表，下载文件
 * 基于：HttpURLConnection
 */
public class FileDownloadThread extends Thread {
    private final String dir;
    private final String urlStr; // 下载URL
    private IOnFileDownloadListener listener;
    private final int id;
    boolean interupted = false; // 是否强制停止下载线程
    private int progress;//当前下载进度

    private FileDownloadThread(Builder builder) {
        dir = builder.dir;
        urlStr = builder.urlStr;
        listener = builder.listener;
        id = builder.id;
    }

    public static Builder newBuilder(String dir, String urlStr, int id) {
        return new Builder(dir, urlStr, id);
    }

    public int getProgress() {
        return progress;
    }

    /**
     * 强行终止文件下载
     */
    public void cancelDownload() {
        interupted = true;
    }

    @Override
    public void run() {
        try {
            StringBuffer mFilePath = new StringBuffer();
            mFilePath.append(dir);
            mFilePath.append(urlStr.replace("/", "_"));
            URL mUrl = new URL(urlStr.toString());
            HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
            conn.setRequestProperty("Accept", "*/*");//设置接收的返回类型
            conn.connect();

            Map<String, List<String>> temp = conn.getHeaderFields();
            Log.i("文件列表下载", "url=" + urlStr);
            int max = conn.getContentLength();
            listener.onStart(id, max);
            InputStream is = conn.getInputStream();//PDF等文件
            File file = new File(mFilePath.toString());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            int len;
            byte[] buffer = new byte[2048];
            int total = 0;
            while (interupted == false && (len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                progress = (int) Math.rint(total * 1.0 / max * 100f);
                listener.onProgress(id, progress);//四舍五入
            }
            fos.flush();
            fos.close();
            is.close();
            if (interupted == true) {
                if (file != null && file.exists() && file.isFile()) {
                    file.delete();
                }
                //                listener = null;
                return;
            }
            if (file != null) {
                listener.onSuccess(id, file);
                return;
            } else {
                Logger.d("FileDownloadThread: 文件下载失败 urlStr=" + urlStr);
                listener.onFail(null);
                return;
            }
        } catch (FileNotFoundException e) {
            listener.onFail(e);
            e.printStackTrace();
        } catch (MalformedURLException e) {
            listener.onFail(e);
            e.printStackTrace();
        } catch (IOException e) {
            listener.onFail(e);
            e.printStackTrace();
        }
    }


    /**
     * Created with Android Studio
     * User:邹旭
     * Date:2017/7/21
     * Time:16:25
     * Desc:下载&上传过程监听
     */
    public interface IOnFileDownloadListener {
        void onSuccess(int id, File file);

        void onStart(int id, int total);

        void onProgress(int id, int progress);

        void onFail(Exception e);

    }

    public static final class Builder {
        private final String dir;
        private final String urlStr;
        private IOnFileDownloadListener listener;
        private final int id;

        private Builder(String dir, String urlStr, int id) {
            this.dir = dir;
            this.urlStr = urlStr;
            this.id = id;
        }

        public Builder listener(IOnFileDownloadListener val) {
            listener = val;
            return this;
        }

        public FileDownloadThread build() {
            return new FileDownloadThread(this);
        }
    }
}
