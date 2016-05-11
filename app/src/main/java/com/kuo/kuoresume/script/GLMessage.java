package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Image;

/**
 * Created by Kuo on 2016/5/9.
 */
public class GLMessage extends ComputeRect {

    private Image shareImage, contactImage;

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 20;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createImage();
        computeRect();
    }

    private void createImage() {

        contactImage = new Image(new RectF(0, 0, 400, 400));
        shareImage = new Image(new RectF(400, 0, 800, 400));

    }

    public void draw(float[] m) {

        contactImage.draw(m, 5);
        shareImage.draw(m, 5);

    }

    public void computeRect() {
        computeContactImage();
        computeSahreImage();
    }

    private void computeContactImage() {

        RectF srcRect = contactImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        contactImage.setDstRect(left, top, right, bottom);

    }

    private void computeSahreImage() {

        RectF srcRect = shareImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        shareImage.setDstRect(left, top, right, bottom);

    }
}
