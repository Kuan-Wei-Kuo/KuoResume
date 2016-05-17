package com.kuo.kuoresume.animation;

/*
 * Created by Kuo on 2016/4/14.
 */
public class SampleAnimation {

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    private OnUpdateListener onUpdateListener;

    public SampleAnimation(long frameLengthInMilliseconds) {
        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
    }

    public void start() {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;


            if(onUpdateListener != null)
                onUpdateListener.onUpdate();

        }

    }

    public interface OnUpdateListener {
        void onUpdate();
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
