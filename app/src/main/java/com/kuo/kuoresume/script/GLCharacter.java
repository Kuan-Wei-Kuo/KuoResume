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

    public static final int HUMAN_IDLE = 0;
    public static final int HUMAN_RUN = 1;
    public static final int HUMAN_JUMP = 2;
    public static final int HUMAN_BAT = 3;
    public static final int HUMAN_UP = 4;
    public static final int HUMAN_DOWN = 5;

    private SpriteAnimation batAnimation;
    private SpriteController deadPoolIdleController, deadPoolRunController, deadPoolDownController, deadPoolUpController;

    private int moveSpeed = 40;
    private int direction = 1;

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

        deadPoolRun = new Image(dstRect);
        deadPoolRun.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);
    }

    public void draw(float[] mvpMatrix) {
        deadPoolRun.draw(mvpMatrix, 0);
    }

    public void computeSprite() {
        deadPoolRunController.start();
    }

    private SpriteController.OnUpdateListener deadPoolRunListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF curRect = viewComputeListener.getViewCompute().getCurRect();

            float width = curRect.width();
            curRect.left -= moveSpeed * direction;
            curRect.right = curRect.left + width;

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

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
