package com.iuicity.smartcollection;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iuicity.smartcollection.utils.RxPermissionManager;
import com.iuicity.smartcollection.utils.StatusBarUtil;
import com.iuicity.smartcollection.utils.Utils;
import com.iuicity.smartcollection.utils.media.MediaPlayerHelper;
import com.iuicity.smartcollection.utils.media.MediaRecorderHelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Unbinder;

public class MainActivity extends FragmentActivity {

    @BindView(R.id.iv_notice)
    ImageView mIvNotice;
    @BindView(R.id.tv_service)
    TextView mTvService;
    private Unbinder mBind;
    private MediaRecorderHelper mMediaRecorderHelper;
    private ServicePopup mServicePopup;
    List<String> mData = Arrays.asList("保险", "催收", "其他");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBind = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        StatusBarUtil.stateBarSetting(this, null, false);
        new RxPermissionManager(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        initMediaRecorder();
                    }
                });

        mTvService.setText(mData.get(0));
    }

    private void initMediaRecorder() {
        mMediaRecorderHelper = new MediaRecorderHelper(getRecorderFilePath());

    }

    public String getRecorderFilePath() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = getExternalCacheDir().getAbsolutePath();
        } else {
            path = getCacheDir().getAbsolutePath();

        }
        return path + File.separator + "Recorder";
    }

    @Override
    protected void onDestroy() {
        mBind.unbind();
        super.onDestroy();
    }

    @OnTouch(R.id.iv_notice)
    public boolean recorderTouchEvent(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mMediaRecorderHelper.startRecord();
            case MotionEvent.ACTION_MOVE:
                mIvNotice.setImageResource(R.mipmap.voice_press);
                break;
            case MotionEvent.ACTION_UP:
                mMediaRecorderHelper.stopAndRelease();
                mIvNotice.setImageResource(R.mipmap.voice_normal);
                play();
                break;
        }
        return true;
    }

    private void play() {
        //播放录音
        MediaPlayerHelper.playSound(mMediaRecorderHelper.getCurrentFilePath());
    }

    @OnClick(R.id.tv_service)
    public void onViewClicked() {
        if (mServicePopup == null) {
            mServicePopup = new ServicePopup(mTvService, this, mData, select -> {
                mTvService.setText(select);
            });
        }
        mServicePopup.show();
    }
}
