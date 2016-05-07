package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kuo.kuoresume.thread.ResumeThread;

/**
 * Created by Kuo on 2016/4/18.
 */
public abstract class AbsResumeView extends SurfaceView implements SurfaceHolder.Callback{

    private ResumeThread resumeThread;

    private SurfaceHolder surfaceHolder;

    public boolean isLoaded = false;

    public AbsResumeView(Context context) {
        this(context, null, 0);
    }

    public AbsResumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsResumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resumeThread = new ResumeThread(this, holder);
        resumeThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        isLoaded = false;
        onLoad(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public abstract void onResumeDraw(Canvas canvas);

    public abstract boolean onLoad(int width, int height);
}
