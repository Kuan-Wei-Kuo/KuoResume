package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SpriteAnimation;
import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Image;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLCharacter extends ComputeRect{

    public static final int CHARACTER_IDLE = 0;
    public static final int CHARACTER_RUN = 1;
    public static final int CHARACTER_JUMP = 2;
    public static final int CHARACTER_UP = 3;
    public static final int CHARACTER_DOWN = 4;
    public static final int CHARACTER_BAT = 5;

    public int CHARACTER_STATE;

    private SpriteAnimation ownAirAnimation;
    private SpriteController deadPoolIdleController, deadPoolRunController, deadPoolDownController, deadPoolUpController;

    private int moveSpeed = 70;
    private int direction = 1, airDirection = 1;

    private Image deadPoolRun;

    public GLCharacter(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        width = (int) viewComputeListener.getViewCompute().getPlantSize();

        height = width * 2;

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        dstRect.set(contentRect.centerX() - width / 2,
                contentRect.bottom - width - height,
                contentRect.centerX() + width / 2,
                contentRect.bottom - width);

        deadPoolRunController = new SpriteController(300,
                objectListener.getHolderBitmap().deadPool_run.getWidth() / 2,
                objectListener.getHolderBitmap().deadPool_run.getHeight(), 2);
        deadPoolRunController.setOnUpdateListener(deadPoolRunListener);

        deadPoolDownController = new SpriteController(10,
                objectListener.getHolderBitmap().deadPool_down.getWidth() / 2,
                objectListener.getHolderBitmap().deadPool_down.getHeight(), 2);
        deadPoolDownController.setOnUpdateListener(deadPoolDownListener);

        deadPoolRun = new Image(dstRect);
        deadPoolRun.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        deadPoolRun = new Image(dstRect);
        deadPoolRun.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);
        CHARACTER_STATE = CHARACTER_IDLE;
    }

    public void draw(float[] mvpMatrix) {

        switch (CHARACTER_STATE) {
            case CHARACTER_IDLE:
                break;
            case CHARACTER_RUN:
                deadPoolRun.draw(mvpMatrix, 0);
                break;
            case CHARACTER_UP:
                deadPoolRun.draw(mvpMatrix, 2);
                break;
            case CHARACTER_DOWN:
                break;
        }

    }

    public void computeSprite(int CHARACTER_STATE) {

        switch (CHARACTER_STATE) {
            case CHARACTER_IDLE:
                break;
            case CHARACTER_RUN:
                deadPoolRunController.start();
                break;
            case CHARACTER_UP:
                deadPoolDownController.start();
                break;
            case CHARACTER_DOWN:
                deadPoolDownController.start();
                break;
        }
    }

    private SpriteController.OnUpdateListener deadPoolRunListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF curRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            float width = curRect.width();

            float curLeft = curRect.left - moveSpeed * direction;
            float curRight = curLeft + width;

            if(curLeft > contentRect.left) {
                curLeft = contentRect.left;
                curRight = curLeft + width;
            } else if(curRight < contentRect.right) {
                curRight = contentRect.right;
                curLeft = contentRect.right - width;
            }

            curRect.left = curLeft;
            curRect.right = curRight;

            float left = (float) (currentHorizontalFrame * deadPoolRunController.getFrameWidth()) / deadPoolRunController.getWidth();
            float right = left + (float) deadPoolRunController.getFrameWidth() / deadPoolRunController.getWidth();
            float top = 0;
            float bottom = top + deadPoolRunController.getFrameHeight() / deadPoolRunController.getWidth();

            deadPoolRun.setUVS(new float[] {
                    left, top,
                    left, bottom,
                    right, bottom,
                    right, top
            });
        }
    };

    private SpriteController.OnUpdateListener deadPoolDownListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            float height = currentRect.height();

            float top = currentRect.top + moveSpeed * airDirection;
            float bottom = top + height;

            if(top > contentRect.top) {
                top = contentRect.top;
                bottom = top + height;
            } else if(contentRect.bottom > bottom) {
                bottom = contentRect.bottom;
                top = bottom - height;
            }

            currentRect.top = top;
            currentRect.bottom = bottom;

            //int left = currentHorizontalFrame * deadPoolDownController.getFrameWidth();
            //int right = left + deadPoolDownController.getFrameWidth();
            //int top = 0;
            //int bottom = top + deadPoolDownController.getFrameHeight();

            deadPoolRun.setUVS(new float[] {
                    0.0f, 0.0f, //left top
                    0.0f, 1.0f, //left bottom
                    1.0f, 1.0f, //right bottom
                    1.0f, 0.0f, //right top
            });

        }
    };


    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setAirDirection(int airDirection) {
        this.airDirection = airDirection;
    }

    public void setCharacterState(int state) {
        CHARACTER_STATE = state;
    }

    public int getDirection() {
        return direction;
    }

    public int getCharacterState() {
        return CHARACTER_STATE;
    }
}
