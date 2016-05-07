package com.kuo.kuoresume.animation;

/*
 * Created by Kuo on 2016/4/14.
 */
public class SampleAnimation {

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    protected int count, frameCount = 0;

    private OnUpdateListener onUpdateListener;

    public SampleAnimation(long frameLengthInMilliseconds, int count) {
        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
        this.count = count;
    }

    public void start() {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;

            frameCount++;

            if(onUpdateListener != null) {
                onUpdateListener.onUpdate();
                if(frameCount >= count)
                    onUpdateListener.onEnd();
            }
        }

    }

    public interface OnUpdateListener {
        void onUpdate();
        void onEnd();
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }
}
