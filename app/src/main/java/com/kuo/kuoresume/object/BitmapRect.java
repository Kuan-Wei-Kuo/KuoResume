package com.kuo.kuoresume.object;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Kuo on 2016/4/21.
 */
public class BitmapRect {

    protected Rect spriteRect;

    protected RectF srcRect;

    protected RectF dstRect = new RectF(0, 0, 0, 0);

    public BitmapRect(RectF srcRect) {

        this.srcRect = srcRect;
    }

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    public void computeDstRect(RectF srcDstRect) {

        float left = srcDstRect.left + srcRect.left;
        float top = srcDstRect.height() - srcRect.height();
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        setDstRect(left, top, right, bottom);
    }

    public RectF getDstRect() {
        return dstRect;
    }

    public RectF getSrcRect() {
        return srcRect;
    }

    public void draw(Canvas canvas, Bitmap bitmap) {

        if(spriteRect == null)
            spriteRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, spriteRect, dstRect, null);
    }
}
