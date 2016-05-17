package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.animation.SpriteAnimation;
import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.object.TalkWindow;
import com.kuo.kuoresume.until.Until;

/*
 * Created by Kuo on 2016/4/13.
 */
public class Human {

    Rect dstRect, downDstRect, srcRect = new Rect();

    public static final int HUMAN_IDLE = 0;
    public static final int HUMAN_RUN = 1;
    public static final int HUMAN_JUMP = 2;
    public static final int HUMAN_BAT = 3;
    public static final int HUMAN_DOWN = 4;
    public static final int HUMAN_UP = 5;

    private TalkWindow mTalkWindow;

    int state = 0;

    int moveSpeed = 20;

    int plant;

    Bitmap spBitmap;

    SpriteAnimation batAnimation;
    SpriteController deadPoolIdleController, deadPoolRunController, deadPoolDownController, deadPoolUpController;

    ObjectListener objectListener;

    public Human(Context context, Rect dstRect, ObjectListener objectListener) {

        this.dstRect = dstRect;
        this.downDstRect = new Rect(dstRect.left,
                dstRect.bottom - dstRect.width(),
                dstRect.left + dstRect.height(),
                dstRect.bottom);

        this.objectListener = objectListener;

        prepareBitmap(context);

        plant = dstRect.bottom;

        batAnimation = new SpriteAnimation(50, new Rect(0, 0, spBitmap.getWidth(), spBitmap.getHeight()), 192, 192, 1);
        batAnimation.setOnUpdateListener(batUpdateListener);

        deadPoolDownController = new SpriteController(25,
                objectListener.getHolderBitmap().deadPool_down.getWidth(),
                objectListener.getHolderBitmap().deadPool_down.getHeight(), 1);
        deadPoolDownController.setOnUpdateListener(deadPoolDownListener);

        deadPoolIdleController = new SpriteController(1000,
                objectListener.getHolderBitmap().deadPool_idle.getWidth(),
                objectListener.getHolderBitmap().deadPool_idle.getHeight(), 1);
        deadPoolIdleController.setOnUpdateListener(deadPoolIdleListener);

        deadPoolRunController = new SpriteController(300,
                objectListener.getHolderBitmap().deadPool_run.getWidth() / 2,
                objectListener.getHolderBitmap().deadPool_run.getHeight(), 2);
        deadPoolRunController.setOnUpdateListener(deadPoolListener);

        deadPoolUpController = new SpriteController(25,
                objectListener.getHolderBitmap().spider_deadpool.getWidth(),
                objectListener.getHolderBitmap().spider_deadpool.getHeight(), 1);
        deadPoolUpController.setOnUpdateListener(deadPoolUpListener);

        mTalkWindow = new TalkWindow(context, "", Until.dp2px(context.getResources().getDisplayMetrics().density, 20));
    }

    public void drawHuman(Canvas canvas) {

        switch (state) {
            case HUMAN_IDLE:
                canvas.drawBitmap(objectListener.getHolderBitmap().deadPool_idle, srcRect, dstRect, null);
                break;
            case HUMAN_RUN:
                canvas.drawBitmap(objectListener.getHolderBitmap().deadPool_run, srcRect, dstRect, null);
                break;
            case HUMAN_JUMP:
                canvas.drawBitmap(objectListener.getHolderBitmap().deadPool_down, srcRect, dstRect, null);
                break;
            case HUMAN_DOWN:
                canvas.drawBitmap(objectListener.getHolderBitmap().deadPool_down, srcRect, downDstRect, null);
                break;
            case HUMAN_UP:
                canvas.drawBitmap(objectListener.getHolderBitmap().spider_deadpool, srcRect, dstRect, null);
                break;
            case HUMAN_BAT:
                canvas.drawBitmap(spBitmap, srcRect, dstRect, null);
                break;

        }

    }

    public void update(int state) {

        switch (state) {
            case HUMAN_IDLE:
                deadPoolIdleController.start();
                break;
            case HUMAN_RUN:
                deadPoolRunController.start();
                break;
            case HUMAN_BAT:
                batAnimation.start();
                break;
            case HUMAN_DOWN:
                deadPoolDownController.start();
                break;
            case HUMAN_UP:
                deadPoolUpController.start();
                break;
        }
        this.state = state;
    }

    private int direction = 1;

    public void setDirection(int direction) {
        this.direction = direction;
    }

    private SpriteAnimation.OnUpdateListener batUpdateListener = new SpriteAnimation.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame, int currentVerticalFrame) {

            RectF curRect = objectListener.getViewCompute().getCurRect();

            float width = curRect.width();
            curRect.left -= moveSpeed * direction;
            curRect.right = curRect.left + width;

            int left = currentHorizontalFrame * batAnimation.getFrameWidth();
            int right = left + batAnimation.getFrameWidth();
            int top = currentVerticalFrame * batAnimation.getFrameHeight();
            int bottom = top + batAnimation.getFrameHeight();

            srcRect.set(left, top, right, bottom);

        }

        @Override
        public void end() {

        }
    };

    int testCount = 0;

    private SpriteController.OnUpdateListener deadPoolDownListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF currentRect = objectListener.getViewCompute().getCurRect();
            RectF contentRect = objectListener.getViewCompute().getContentRect();

            float height = currentRect.height();

            float curTop = currentRect.top - moveSpeed;
            float curBottom = curTop + height;

            if(contentRect.bottom > curBottom) {
                curBottom = contentRect.bottom;
                curTop = contentRect.bottom - height;
                state = HUMAN_IDLE;
                testCount = 0;
            }

            if(testCount > 200 || testCount == 0) {
                currentRect.top = curTop;
                currentRect.bottom = curBottom;
            }


            int left = currentHorizontalFrame * deadPoolDownController.getFrameWidth();
            int right = left + deadPoolDownController.getFrameWidth();
            int top = 0;
            int bottom = top + deadPoolDownController.getFrameHeight();

            srcRect.set(left, top, right, bottom);

            testCount++;
        }

        @Override
        public void onEnd() {

        }
    };

    private SpriteController.OnUpdateListener deadPoolUpListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF currentRect = objectListener.getViewCompute().getCurRect();
            RectF contentRect = objectListener.getViewCompute().getContentRect();

            float height = currentRect.height();

            float curTop = currentRect.top + moveSpeed;
            float curBottom = curTop + height;

            if(curTop > contentRect.top) {
                curTop = contentRect.top;
                curBottom = curTop + height;
                state = HUMAN_IDLE;
            }

            currentRect.top = curTop;
            currentRect.bottom = curBottom;

            int left = currentHorizontalFrame * deadPoolUpController.getFrameWidth();
            int right = left + deadPoolUpController.getFrameWidth();
            int top = 0;
            int bottom = top + deadPoolUpController.getFrameHeight();

            srcRect.set(left, top, right, bottom);
        }

        @Override
        public void onEnd() {

        }
    };

    private SpriteController.OnUpdateListener deadPoolIdleListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            int left = currentHorizontalFrame * deadPoolIdleController.getFrameWidth();
            int right = left + deadPoolIdleController.getFrameWidth();
            int top = 0;
            int bottom = top + deadPoolIdleController.getFrameHeight();

            srcRect.set(left, top, right, bottom);

        }

        @Override
        public void onEnd() {

        }
    };

    private SpriteController.OnUpdateListener deadPoolListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF curRect = objectListener.getViewCompute().getCurRect();

            float width = curRect.width();
            curRect.left -= moveSpeed * direction;
            curRect.right = curRect.left + width;

            int left = currentHorizontalFrame * deadPoolRunController.getFrameWidth();
            int right = left + deadPoolRunController.getFrameWidth();
            int top = 0;
            int bottom = top + deadPoolRunController.getFrameHeight();

            srcRect.set(left, top, right, bottom);

        }

        @Override
        public void onEnd() {

        }
    };

    private void prepareBitmap(Context context) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        spBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.bat, options);

    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
