package com.jasonzou.retrofitdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


/**
 * 图片压缩工具类
 * <p>尺寸压缩，质量压缩</p>
 *
 * @author Administrator
 */
public class ImageCompressUtils {

    public static final String CONTENT = "content";
    public static final String FILE = "file";

    /**
     * 图片压缩参数
     *
     * @author Administrator
     */
    public static class CompressOptions {
        public static final int DEFAULT_WIDTH = 400;
        public static final int DEFAULT_HEIGHT = 800;

        public int maxWidth = DEFAULT_WIDTH;
        public int maxHeight = DEFAULT_HEIGHT;
        /**
         * 压缩后图片保存的文件
         */
        public File destFile;
        /**
         * 图片压缩格式,默认为jpg格式
         */
        public CompressFormat imgFormat = CompressFormat.JPEG;

        /**
         * 图片压缩比例 默认为30
         */
        public int quality = 30;

        public Uri uri;
    }

    public Bitmap compressFromUri(Context context,
                                  CompressOptions compressOptions) {

        // uri指向的文件路径
        String filePath = getFilePath(context, compressOptions.uri);

        if (null == filePath) {
            return null;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

         BitmapFactory.decodeFile(filePath, options);

        int actualWidth = options.outWidth;
        int actualHeight = options.outHeight;

        int desiredWidth = getResizedDimension(compressOptions.maxWidth,
                compressOptions.maxHeight, actualWidth, actualHeight);
        int desiredHeight = getResizedDimension(compressOptions.maxHeight,
                compressOptions.maxWidth, actualHeight, actualWidth);

        options.inJustDecodeBounds = false;
        options.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                desiredWidth, desiredHeight);

        //腾讯高级开发工程师(解决out of memory)
        options.inPurgeable = true;
        options.inInputShareable = true;

        Bitmap bitmap = null;

        //第一次压缩 - 尺寸压缩(展示)
        Bitmap destBitmap = BitmapFactory.decodeFile(filePath, options);

        // If necessary, scale down to the maximal acceptable size.
        if (destBitmap.getWidth() > desiredWidth
                || destBitmap.getHeight() > desiredHeight) {
            bitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth,
                    desiredHeight, true);
            destBitmap.recycle();
        } else {
            bitmap = destBitmap;
        }

        // compress file if need
        //第二次压缩 - 质量压缩(存储)
        if (null != compressOptions.destFile) {
            compressFile(compressOptions, bitmap);
        }

        return bitmap;
    }

    /**图片质量压缩
     * <p>让图片经行重组，通过改变色深和透明度来经行压缩，但是像素是没有改变的，所以是不会减少bitmap占用的内存。</p>
     * <p>只是改变其存储的形式的大小,不改变bitmap在内存中的大小</p>
     * <p>一般用于不失真压缩，上传图片等</p>
     * compress file from bitmap with compressOptions
     *
     * @param compressOptions
     * @param bitmap
     */
    private void compressFile(CompressOptions compressOptions, Bitmap bitmap) {
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(compressOptions.destFile);
        } catch (FileNotFoundException e) {
            Log.i("ImageCompress", e.getMessage());
        }

        bitmap.compress(compressOptions.imgFormat, compressOptions.quality,
                stream);
    }

    private static int findBestSampleSize(int actualWidth, int actualHeight,
                                          int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }

        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        // If no dominant value at all, just return the actual.
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }

        // If primary is unspecified, scale primary to match secondary's scaling
        // ratio.
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * 获取文件的路径
     *
     * @param uri
     * @return
     */
    private String getFilePath(Context context, Uri uri) {

        String filePath = null;

        if (CONTENT.equalsIgnoreCase(uri.getScheme())) {

            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{Images.Media.DATA}, null, null, null);

            if (null == cursor) {
                return null;
            }

            try {
                if (cursor.moveToNext()) {
                    filePath = cursor.getString(cursor
                            .getColumnIndex(Images.Media.DATA));
                }
            } finally {
                cursor.close();
            }
        }

        // 从文件中选择
        if (FILE.equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }

        return filePath;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */

    public static Bitmap rotateBitmapByDegree(Bitmap bm, float degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片属性：旋转的角度
     *<p><b>小米，三星</b>手机拍摄的相片存在旋转角度-图片尺寸与横竖不符</p>
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int getPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static void printImagesSpec(List<String> paths) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        for (String path : paths) {
            BitmapFactory.decodeFile(path, options);
            Logger.d(String.format("path=%s\noutHeight=%d,outWidth=%d,degree=%d", path, options.outHeight, options.outWidth, getPictureDegree(path)));

            /*
            //覆写图片参数
            try {
                ExifInterface exif = new ExifInterface(path);
                exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(ExifInterface.ORIENTATION_NORMAL));
                exif.saveAttributes();
                Logger.d(String.format("step2: path=%s\noutHeight=%d,outWidth=%d,degree=%d", path, options.outHeight, options.outWidth, getPictureDegree(path)));
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }

    /**
     * Uri转Path
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getUriToPath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public static boolean isImageFileExists(String path) {
        if (!TextUtils.isEmpty(path)) {
            File file = new File(path);
            return file.exists() && file.isFile();
        }
        return false;
    }

    /**
     * 保存图片
     * <p>文件覆盖</p>
     * @param path 存储地址
     * @param bm
     * @return 保存了图片的文件
     */
    public static File saveBitmapReturnFile(Bitmap bm, String path) {
        File f = null;
        try {
            f = new File(path);
            if (!f.getParentFile().exists()) {
                f.mkdirs();
            }
            if (f.exists()) {
                f.delete();
            }
            //            Logger.d("bitmap保存至" + path);
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {
            return f;
        }
    }
}

//压缩图片用法 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//ImageCompress compress = new ImageCompress();            
//ImageCompress.CompressOptions options = new ImageCompress.CompressOptions();             
//options.uri = imgUri;            
//options.maxWidth=getWindowManager().getDefaultDisplay().getWidth();             
//options.maxHeight=getWindowManager().getDefaultDisplay().getHeight();             
//Bitmap bitmap = compress.compressFromUri(this, options); 
//压缩图片用法 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
