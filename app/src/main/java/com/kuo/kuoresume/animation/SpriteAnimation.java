package com.kuo.kuoresume.animation;

import android.content.Context;
import android.graphics.Rect;

/**
 * Created by Kuo on 2016/4/13.
 */
public class SpriteAnimation {

    protected Context context;

    protected Rect bitmapRect;

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    protected int countHorizontalFrame = 0, countVerticalFrame = 0, countFrame = 0;
    protected int currentHorizontalFrame = 0, currentVerticalFrame = 0;
    protected int frameWidth = 0;
    protected int frameHeight = 0;

    private OnUpdateListener onUpdateListener;

    public SpriteAnimation(long frameLengthInMilliseconds, Rect bitmapRect, int frameWidth, int frameHeight, int countFrame) {
        //this.context = context;
        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
        this.bitmapRect = bitmapRect;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.countFrame = countFrame;

        countHorizontalFrame = bitmapRect.width() / frameWidth;
        countVerticalFrame = bitmapRect.height() / frameHeight;
    }

    private int animateCount = 0;

    public void start() {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;

            currentHorizontalFrame++;

            if (currentHorizontalFrame >= countHorizontalFrame) {

                currentHorizontalFrame = 0;

                currentVerticalFrame++;

                if(currentVerticalFrame >= countVerticalFrame)
                    currentVerticalFrame = 0;
            }

            animateCount++;
        }

        if(onUpdateListener != null) {

            onUpdateListener.onUpdate(currentHorizontalFrame, currentVerticalFrame);

            if(animateCount >= countFrame) {
                currentVerticalFrame = 0;
                currentHorizontalFrame = 0;
                animateCount = 0;
                onUpdateListener.end();
            }
        }

    }

    public interface OnUpdateListener {
        void onUpdate(int currentHorizontalFrame, int currentVerticalFrame);
        void end();
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

    public int getCountFrame() {
        return countFrame;
    }
}
