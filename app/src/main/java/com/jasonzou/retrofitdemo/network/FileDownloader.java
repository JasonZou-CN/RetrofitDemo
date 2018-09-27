package com.jasonzou.retrofitdemo.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 文件下载列表，下载文件
 * 基于：HttpURLConnection
 */
public class FileDownloader {
    private final String urlStr; // 下载URL
    private final File fileSaved;
    private final int id;
    private final int START = 0;
    private final int PROJRESS = 1;
    private final int SUCCESS = 2;
    private final int FAIL = -1;
    boolean interupted = false; // 是否强制停止下载线程
    private IOnFileDownloadListener listener;
    private int progress;//当前下载进度
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (listener == null)
                return;
            Object[] objs;
            switch (msg.what) {
                case START:
                    objs = (Object[]) msg.obj;
                    listener.onStart((Integer) objs[0], (Integer) objs[1]);
                    break;
                case PROJRESS:
                    objs = (Object[]) msg.obj;
                    listener.onProgress((Integer) objs[0], (Integer) objs[1]);
                    break;
                case SUCCESS:
                    objs = (Object[]) msg.obj;
                    listener.onSuccess((Integer) objs[0], (File) objs[1]);
                    break;
                case FAIL:
                    objs = (Object[]) msg.obj;
                    listener.onFail((Integer) objs[0], (Exception) objs[1]);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private FileDownloader(Builder builder) {
        urlStr = builder.urlStr;
        fileSaved = builder.fileSaved;
        id = builder.id;
        listener = builder.listener;
    }

    public static Builder newBuilder(String urlStr, File fileSaved, int id) {
        return new Builder(urlStr, fileSaved, id);
    }

    public static Builder newBuilder(String urlStr, File fileSaved) {
        return new Builder(urlStr, fileSaved, 0);
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

    public FileDownloader download() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Message msg;
                    /*StringBuffer mFilePath = new StringBuffer();
                    mFilePath.append(dir);
                    mFilePath.append(File.separator);
                    mFilePath.append(urlStr.replace("/", "_"));*/
                    URL mUrl = new URL(urlStr.toString());
                    HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
                    conn.setRequestProperty("Accept", "*/*");//设置接收的返回类型
                    conn.connect();

                    Map<String, List<String>> temp = conn.getHeaderFields();
                    int max = conn.getContentLength();
                    Logger.d("文件下载：" + urlStr);
                    sendMsg(START, new Object[]{id, max});

                    InputStream is = conn.getInputStream();//PDF等文件
                    File file = fileSaved;
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[2048];
                    int total = 0;
                    while (interupted == false && (len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;
                        progress = (int) Math.rint(total * 1.0 / max * 100f);

                        Logger.d("下载进度:" + progress);
                        sendMsg(PROJRESS, new Object[]{id, progress});
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
                        Logger.d("下载成功:" + file.getAbsolutePath());
                        sendMsg(SUCCESS, new Object[]{id, file});
                        return;
                    } else {
                        Logger.d("下载失败:文件未写入");
                        sendMsg(FAIL, new Object[]{id, null});
                        return;
                    }
                } catch (Exception e) {
                    Logger.d("下载失败:" + e.toString());
                    sendMsg(FAIL, new Object[]{id, e});
                }/*catch (FileNotFoundException e) {
                    //服务器不存在该文件
                    listener.onSomeDenied(e);
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    listener.onSomeDenied(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    listener.onSomeDenied(e);
                    e.printStackTrace();
                } */
            }
        }).start();
        return this;
    }

    private void sendMsg(int status, Object[] data) {
        Message msg;
        msg = handler.obtainMessage();
        msg.what = status;
        msg.obj = data;
        handler.sendMessage(msg);
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

        void onFail(Integer obj, Exception e);

    }

    public static final class Builder {
        private final String urlStr;
        private final File fileSaved;
        private final int id;
        private IOnFileDownloadListener listener;

        private Builder(@NonNull String urlStr, @NonNull File fileSaved, int id) {
            this.urlStr = urlStr;
            this.fileSaved = fileSaved;
            this.id = id;
        }

        private Builder(@NonNull String urlStr, @NonNull File fileSaved) {
            this.urlStr = urlStr;
            this.fileSaved = fileSaved;
            this.id = 0;
        }


        public Builder listener(IOnFileDownloadListener val) {
            listener = val;
            return this;
        }

        public FileDownloader build() {
            return new FileDownloader(this);
        }
    }
}
