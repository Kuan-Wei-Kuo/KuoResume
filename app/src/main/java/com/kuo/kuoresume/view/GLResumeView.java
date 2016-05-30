package com.kuo.kuoresume.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.presents.HolderBitmap;
import com.kuo.kuoresume.renderer.InterviewRenderer;

/**
 * Created by Kuo on 2016/5/4.
 */
public class GLResumeView extends GLSurfaceView {

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

        mInterviewRenderer = new InterviewRenderer(context, activityListener);
        mInterviewRenderer.setOnGLViewListener(onRendererListener);
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

    public interface OnGLViewListener {
        void onFirstDraw();
    }

    private OnGLViewListener onGLViewListener;

    public void setOnGLViewListener(OnGLViewListener onGLViewListener) {
        this.onGLViewListener = onGLViewListener;
    }

    private InterviewRenderer.OnRendererListener onRendererListener = new InterviewRenderer.OnRendererListener() {
        @Override
        public void onFirstDraw() {
            if (onGLViewListener != null)
                onGLViewListener.onFirstDraw();
        }
    };
}
