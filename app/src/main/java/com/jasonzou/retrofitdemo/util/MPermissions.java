package com.jasonzou.retrofitdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with Android Studio<br/>
 * User:邹旭<br/>
 * Date:2017/7/28<br/>
 * Time:10:04<br/>
 * Desc:<br/>
 * Andrsoid M 动态权限解决方案 <br/>
 * 1.单一权限的动态申请，<br/>
 * 2.处理了“拒绝”，并且”不再提醒“的情况<br/>
 * 3.一个实例只处理一个或者一组权限请求<br/>
 */

public class MPermissions {
    private static final String TAG = "MPermissions";
    private static MPermissions mPermissions;
    final String LOCAL_SETTING = "permissions_requested";
    private ICalllback iCalllback;
    private Fragment fragment;
    private Activity activity;
    private Context ctx;
    private int requestCode = -1;//初始为-1
    private int reqStatus = 0;//0：未请求；1：已请求；2：已授权；3：已拒绝；4：不再提醒；
    private String[] permissions = null;
    private String permissionDesc = "当前应用缺少必要权限，相关功能暂时无法使用。如若需要，请单击【确定】按钮进行授权。";
    private String permission = null;

    /**
     * 不对外使用
     */
    private MPermissions() {}

    public static MPermissions init(Fragment fragment, ICalllback iCalllback) {
        mPermissions = new MPermissions();
        mPermissions.fragment = fragment;
        mPermissions.ctx = fragment.getContext();
        mPermissions.iCalllback = iCalllback;
        return mPermissions;
    }

    public static MPermissions init(Activity activity, ICalllback iCalllback) {
        mPermissions = new MPermissions();
        mPermissions.activity = activity;
        mPermissions.ctx = activity.getBaseContext();
        mPermissions.iCalllback = iCalllback;
        return mPermissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    /**
     * @return 0：未请求；1：已请求；2：已授权；3：已拒绝；4：不再提醒；
     */
    public int getReqStatus() {
        return reqStatus;
    }

    /**
     * 设置权限介绍
     *
     * @param permissionDesc
     */
    public void setPermissionDesc(String permissionDesc) {
        this.permissionDesc = permissionDesc;
    }

    /**
     * 请求权限
     *
     * @param requestCode
     * @param permission
     */
    public MPermissions reqPermission(final int requestCode, final String permission) {
        if (this.permission != null && !this.permission.equals(permission) || this.permissions != null)
            throw new IllegalArgumentException("one MPermissions instance only can request one permission or one group permission");
        this.requestCode = requestCode;
        this.permission = permission;
        /*Android M*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            iCalllback.onSuccess();
            return this;
        }

        reqStatus = 1;
        /*权限监测*/
        if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED) {
            if (isPermissionFirstReq(new String[]{permission})) {
                /*区别对待Fragment和Activity的权限申请方式:权限申请*/
                if (fragment == null)
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                else
                    fragment.requestPermissions(new String[]{permission}, requestCode);
                makePermissionRequested(new String[]{permission});
            } else {
                /*【点了"拒绝"，没点"不再询问】"*/
                /*【首次请求权限，这里也是返回false】*/
                if (activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || fragment != null && fragment.shouldShowRequestPermissionRationale(permission)) {
                    if (fragment == null)
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    else
                        fragment.requestPermissions(new String[]{permission}, requestCode);
                } else {

                    /*【点了"拒绝"，同时点了"不再询问",后续对于权限的请求皆为“拒绝“】*/

                    /*解决：Unable to add window -- token null is not valid; is your activity running?
                     *       Builder(Activity)
                     * */

                    /*解决：You need to use a Theme.AppCompat theme (or descendant) with this activity.
                     *       android.app.AlertDialog，摒弃V7包下的
                     * */
                    new AlertDialog.Builder(fragment == null ? activity : fragment.getActivity(), android.support.v7.appcompat.R.style.Base_Theme_AppCompat_Dialog_Alert).setTitle("提示信息").setMessage(permissionDesc).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + ctx.getPackageName()));
                            ctx.startActivity(intent);
                        }
                    }).setCancelable(false).show();
                }

            }
        } else {//有权限
            reqStatus = 2;
            iCalllback.onSuccess();
        }
        return this;
    }

    /**
     * 请求权限-一组权限
     *
     * @param requestCode
     * @param permissions
     */
    public MPermissions reqPermissions(final int requestCode, final String[] permissions) {
        if (this.permissions != null && this.permissions.length == permissions.length) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (!permissions[i].equals(this.permissions[i]))
                    throw new IllegalArgumentException("one MPermissions instance only can request one permission or one group permission");
            }
        } else if (this.permission != null)
            throw new IllegalArgumentException("one MPermissions instance only can request one permission or one group permission");

        this.requestCode = requestCode;
        this.permissions = permissions;
        /*Android M*/
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            iCalllback.onSuccess();
            return this;
        }

        List<String> declinePermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED)
                declinePermissions.add(permission);
        }

        reqStatus = 1;
        /*权限监测*/
        if (declinePermissions.size() != 0) {
            if (isPermissionFirstReq(permissions)) {
                /*区别对待Fragment和Activity的权限申请方式:权限申请*/
                String[] permissArr = declinePermissions.toArray(new String[declinePermissions.size()]);
                if (fragment == null)
                    ActivityCompat.requestPermissions(activity, permissArr, requestCode);
                else
                    fragment.requestPermissions(permissArr, requestCode);
                makePermissionRequested(declinePermissions.toArray(new String[declinePermissions.size()]));
            } else {

                /*首次请求权限，这里也是返回false*/
                List<String> needReReqPermissions = new ArrayList<>();
                List<String> dontReqAgainPermissions = new ArrayList<>();
                for (String permission : declinePermissions) {
                    if (activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, permission) || fragment != null && fragment.shouldShowRequestPermissionRationale(permission))
                        needReReqPermissions.add(permission);
                    else
                        dontReqAgainPermissions.add(permission);
                }

                /*【点了"拒绝"，没点"不再询问】"*/
                String[] needReqPerms = needReReqPermissions.toArray(new String[needReReqPermissions.size()]);
                if (needReReqPermissions.size() == declinePermissions.size()) {
                    if (fragment == null)
                        ActivityCompat.requestPermissions(activity, needReqPerms, requestCode);
                    else
                        fragment.requestPermissions(needReqPerms, requestCode);
                } else {
                    /*【点了"拒绝"，同时点了"不再询问",后续对于权限的请求皆为“拒绝“】*/

                    /*解决：Unable to add window -- token null is not valid; is your activity running?
                     *       Builder(Activity)
                     * */

                    /*解决：You need to use a Theme.AppCompat theme (or descendant) with this activity.
                     *       android.app.AlertDialog，摒弃V7包下的
                     * */
                    new AlertDialog.Builder(fragment == null ? activity : fragment.getActivity()).setTitle("提示信息").setMessage(permissionDesc).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + ctx.getPackageName()));
                            ctx.startActivity(intent);
                        }
                    }).setCancelable(false).show();
                }
            }
        } else {//有权限
            reqStatus = 2;
            iCalllback.onSuccess();
        }
        return this;
    }

    /**
     * 在Activity或者Fragment的onRequestPermissionsResult中调用
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /*验证*/
        if (requestCode != this.requestCode) {
            return;
        } else if (this.permission != null && !permissions[0].equals(this.permission)) {
            return;
        } else if (this.permissions != null && this.permissions.length == permissions.length) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                if (!permissions[i].equals(this.permissions[i]))
                    return;
            }
        }

        if (permission != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//单一
            reqStatus = 2;
            iCalllback.onSuccess();
            return;
        } else if (permission != null) {
            reqStatus = 3;
            iCalllback.onFail();
            return;
        }
        if (permissions != null) {//一组
            List<String> declines = new ArrayList<>();
            for (int i = 0, len = grantResults.length; i < len; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Logger.d(TAG, "权限申请失败: permission=" + permission);
                    declines.add(permissions[i]);
                }
            }
            if (declines.size() != 0) {
                reqStatus = 3;
                iCalllback.onFail();
                return;
            } else {
                reqStatus = 2;
                iCalllback.onSuccess();
                return;
            }
        }
    }

    /**
     * 检测是否为首次请求该组权限
     *
     * @param permissions
     * @return
     */
    private boolean isPermissionFirstReq(String[] permissions) {
        SharedPreferences sp = ctx.getSharedPreferences(LOCAL_SETTING, Context.MODE_PRIVATE);
        for (String permission : permissions) {
            if (!sp.contains(permission))
                return true;
        }
        return false;
    }

    /**
     * 存储一次权限请求
     *
     * @param permissions
     */
    private void makePermissionRequested(String[] permissions) {
        SharedPreferences sp = ctx.getSharedPreferences(LOCAL_SETTING, Context.MODE_PRIVATE);
        for (String permission : permissions) {
            sp.edit().putBoolean(permission, true).commit();
        }
    }

    public interface ICalllback {
        void onSuccess();

        void onFail();
    }
}
