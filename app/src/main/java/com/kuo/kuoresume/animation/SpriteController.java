package com.kuo.kuoresume.animation;

import android.util.Log;

/**
 * Created by Kuo on 2016/5/1.
 */
public class SpriteController {

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    protected int frameWidth = 0;
    protected int frameHeight = 0;
    protected int countFrame = 0, currentHorizontalFrame = 0;

    private OnUpdateListener onUpdateListener;

    private boolean isEnd = true;

    public SpriteController(long frameLengthInMilliseconds, int frameWidth, int frameHeight, int countFrame) {

        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.countFrame = countFrame;

    }

    public interface OnUpdateListener {
        void onUpdate(int currentHorizontalFrame);
        void onEnd();
    }

    public void start() {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            isEnd = false;

            lastFrameChangeTime = time;

            currentHorizontalFrame++;

            if (currentHorizontalFrame >= countFrame) {

                currentHorizontalFrame = 0;

                isEnd = true;

                if(onUpdateListener != null) {
                    onUpdateListener.onEnd();
                }
            }
        }

        if(onUpdateListener != null)
            onUpdateListener.onUpdate(currentHorizontalFrame);

    }

    private boolean isKeep = false;

    public void keepHorizontalFrame(int horizontalFrame) {

        long time  = System.currentTimeMillis();
        isKeep = true;

        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;

            onUpdateListener.onUpdate(horizontalFrame);
        }
    }

    public void setKeep(boolean keep) {
        isKeep = keep;
    }

    public boolean isKeep() {
        return isKeep;
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener) {
        this.onUpdateListener = onUpdateListener;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getWidth() {
        return frameWidth * countFrame;
    }

    public int getCountFrame() {
        return countFrame;
    }

    public boolean isEnd() {
        return isEnd;
    }
}
