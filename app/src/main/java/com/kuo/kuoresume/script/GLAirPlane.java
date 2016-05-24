package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SampleAnimation;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;

/**
 * Created by Kuo on 2016/5/24.
 */
public class GLAirPlane extends ComputeRect {

    private GLImage airPlane;

    private SampleAnimation upDownAnimation, moveAnimation;

    private boolean isStart = false;

    private float speed = 30;

    public GLAirPlane(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        airPlane = new GLImage(new RectF(srcRect));
        airPlane.computeDstRect(dstRect);

        speed = speed * viewComputeListener.getScaling();

        moveAnimation = new SampleAnimation(10);
        moveAnimation.setOnUpdateListener(onMoveListener);

        upDownAnimation = new SampleAnimation(10);
        upDownAnimation.setOnUpdateListener(onUpDownListener);
    }

    public void draw(float[] m) {
        airPlane.draw(m, 21);
    }

    @Override
    public void setSrcRect(float left, float top, float right, float bottom) {
        super.setSrcRect(left, top, right, bottom);

        airPlane.setSrcRect(srcRect);
    }

    @Override
    public void setDstRect(float left, float top, float right, float bottom) {
        super.setDstRect(left, top, right, bottom);

        computeUVS();

        if(isStart) {
            upDownAnimation.start();
            moveAnimation.start();
        }

        airPlane.computeDstRect(dstRect);
    }

    @Override
    public void computeDstRect(RectF rawDstRect) {
        super.computeDstRect(rawDstRect);

        computeUVS();

        if(isStart) {
            upDownAnimation.start();
            moveAnimation.start();
        }

        airPlane.computeDstRect(dstRect);

    }

    public void start() {
        isStart = true;
    }

    public void end() {
        isStart = false;
    }

    private void computeUVS() {

        int direction = viewComputeListener.getGLCharacter().getDirection();

        if(direction == 1)
            airPlane.setUVS(new float[] {
                    0, 0,
                    0, 1,
                    1, 1,
                    1, 0
            });
        else
            airPlane.setUVS(new float[] {
                    1, 0,
                    1, 1,
                    0, 1,
                    0, 0
            });

    }

    private SampleAnimation.OnUpdateListener onMoveListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            int direction = viewComputeListener.getGLCharacter().getDirection();

            float width = currentRect.width();

            float left = currentRect.left - speed * direction;
            float right = left + width;

            if(left > contentRect.left) {
                left = contentRect.left;
                right = left + width;
            } else if(right < contentRect.right) {
                right = contentRect.right;
                left = right - width;
            }

            currentRect.left = left;
            currentRect.right = right;

        }
    };

    private SampleAnimation.OnUpdateListener onUpDownListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            int direction = viewComputeListener.getGLCharacter().getDirection();

            float height = currentRect.height();

            float top = currentRect.top + speed * direction;
            float bottom = top + height;

            if(top > contentRect.top) {
                top = contentRect.top;
                bottom = top + height;
            } else if(bottom < contentRect.bottom) {
                bottom = contentRect.bottom;
                top = bottom - height;
            }

            currentRect.top = top;
            currentRect.bottom = bottom;
        }
    };



}
