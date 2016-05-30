package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ViewComputeListener;

/**
 * Created by Kuo on 2016/5/5.
 */
public class ComputeRect {

    protected RectF srcRect = new RectF(0, 0, 0, 0);

    protected RectF dstRect = new RectF(0, 0, 0, 0);

    protected float width, height;

    protected int PLANT_RANGE_SIZE;

    protected ViewComputeListener viewComputeListener;

    protected Context context;

    public ComputeRect(Context context, ViewComputeListener viewComputeListener) {

        this.context = context;

        this.viewComputeListener = viewComputeListener;
    }

    public void setSrcRect(float left, float top, float right, float bottom) {
        srcRect.set(left, top, right, bottom);
    }

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    public void setDstRect(RectF dstRect) {
        this.dstRect = dstRect;
    }

    public void computeDstRect(RectF rawDstRect) {

        float left = rawDstRect.left + srcRect.left;
        float top = rawDstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        setDstRect(left, top, right, bottom);
    }

    public RectF getSrcRect() {
        return srcRect;
    }

    public RectF getDstRect() {
        return dstRect;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
}
