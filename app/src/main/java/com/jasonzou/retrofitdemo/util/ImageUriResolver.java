package com.jasonzou.retrofitdemo.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created with Android Studio
 * User:邹旭
 * Date:2017/10/24
 * Time:23:08
 * Desc:Android图库选择KitKat(19) 4.4 的兼容处理<br/>
 * 通过:<br/>
 * Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);<br/>
 * photoPickerIntent.setType("image/*");<br/>
 * startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);<br/>
 * 选择图片<br/>
 * 在：<br/>
 * onActivityResult(int requestCode, int resultCode, Intent data)<br/>
 * 中处理<br/>
 *
 */

public class ImageUriResolver {
    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private static String getImagePath(Uri uri, String selection, Context context) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 4.4及以上系统处理图片的方法
     * case 1: content://media/external/images/media/647991 MEIZU 5.1.1
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String handleImgeAfterKitKat(Uri uri, Context context) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null, context);
            }
            //根据图片路径显示图片
            //            displayImage(imagePath, null);

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null, context);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;

    }

    /**
     * 解析包含图片的Uri中的图片地址(File Path)
     *
     * @param uri 【比如：图片选择】
     * @param ctx
     * @return
     */
    public static String handleImageUri(Uri uri, Context ctx) {
        String path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4及以上系统使用这个方法处理图片
            path = ImageUriResolver.handleImgeAfterKitKat(uri, ctx);
        } else {
            path = ImageUriResolver.handleImageBeforeKitKat(uri, ctx);
        }
        return path;
    }

    /**
     * 4.4以下系统处理图片的方法
     */
    private static String handleImageBeforeKitKat(Uri uri, Context context) {
        String imagePath = getImagePath(uri, null, context);
        return imagePath;
    }

    /**
     * 根据图片路径显示图片的方法
     */
    public static void displayImage(String imagePath, ImageView imageView) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(imageView.getContext(), "failed to getSuitableHeight image", Toast.LENGTH_SHORT).show();
        }
    }
}
