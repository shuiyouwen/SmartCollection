package com.iuicity.smartcollection.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.iuicity.smartcollection.utils.SpUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

/**
 * 6.0权限封装，解决用户选择不再提示问题
 * Created by Shui on 2017/11/21.
 */

public class RxPermissionManager {

    private final Activity mActivity;
    private RxPermissions mRxPermissions;
    private ObservableEmitter<Boolean> mEmitter;

    public RxPermissionManager(Activity activity) {
        mActivity = activity;
        mRxPermissions = new RxPermissions(activity);
    }

    public Observable<Boolean> request(final String... permissions) {
        return Observable.create(e -> {
            mEmitter = e;
            execute(permissions);
        });
    }

    private void execute(final String... permissions) {
        final boolean shouldShowRequestPermissionRationale = (boolean) SpUtils.get(mActivity, getPermissionKey(permissions), false);
        mRxPermissions.shouldShowRequestPermissionRationale(mActivity, permissions)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        //不需要弹窗告诉用户为何申请权限
                        SpUtils.put(mActivity, getPermissionKey(permissions), true);
                        requestPermission(permissions);
                    } else {
                        if (shouldShowRequestPermissionRationale) {
                            //用户上次勾选了不在提示，需要跳转到设置界面
                            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            builder.setMessage("请在设置里面打开权限");
                            builder.setPositiveButton("去设置", (dialog, which) -> {
                                dialog.cancel();
                                goToSetting();
                            });
                            builder.setNegativeButton("否", (dialog, which) -> dialog.cancel());
                            builder.show();
                            if (mEmitter != null) {
                                mEmitter.onNext(false);
                            }
                        } else {
                            //用户第一次进行权限选择，需要弹窗告知用户
                            requestPermission(permissions);
                        }
                    }
                });
    }

    private String getPermissionKey(String[] permissions) {
        StringBuilder result = new StringBuilder();
        for (String permission : permissions) {
            result.append(permission);
        }
        return result.toString();
    }

    private void goToSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", mActivity.getPackageName(), null));
        mActivity.startActivity(localIntent);
    }

    private void requestPermission(final String... permission) {
        mRxPermissions.request(permission)
                .subscribe(aBoolean -> {
                    if (mEmitter != null) {
                        mEmitter.onNext(aBoolean);
                    }
                });
    }

}
