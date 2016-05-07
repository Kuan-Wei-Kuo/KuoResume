package com.kuo.kuoresume.animation;

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

    public SpriteController(long frameLengthInMilliseconds, int frameWidth, int frameHeight, int countFrame) {

        this.frameLengthInMilliseconds = frameLengthInMilliseconds;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.countFrame = countFrame;

    }

    public interface OnUpdateListener {
        void onUpdate(int currentHorizontalFrame);
    }

    public void start() {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;

            currentHorizontalFrame++;

            if (currentHorizontalFrame >= countFrame)
                currentHorizontalFrame = 0;

        }

        if(onUpdateListener != null)
            onUpdateListener.onUpdate(currentHorizontalFrame);

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
}
