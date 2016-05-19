package com.kuo.kuoresume.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.renderer.InterviewRenderer;
import com.kuo.kuoresume.script.Human;

/**
 * Created by Kuo on 2016/5/4.
 */
public class GLResumeView extends GLSurfaceView implements ObjectListener {

    private InterviewRenderer mInterviewRenderer;

    private HolderBitmap holderBitmap;

    public GLResumeView(Context context, ActivityListener activityListener) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
    }

    public GLResumeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

    }

    public void setGLRenderer(Context context, ActivityListener activityListener) {

        //setBackgroundResource(R.mipmap.bg_1);

        mInterviewRenderer = new InterviewRenderer(context, this, activityListener);
        setRenderer(mInterviewRenderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mInterviewRenderer.onTouchEvent(event);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mInterviewRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mInterviewRenderer.onResume();
    }

    @Override
    public HolderBitmap getHolderBitmap() {
        return holderBitmap;
    }

    @Override
    public ViewCompute getViewCompute() {
        return null;
    }

    @Override
    public Human getHuman() {
        return null;
    }
}
