package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.until.Until;

/**
 * Created by Kuo on 2016/5/6.
 */
public class GLImageText {

    public static final float WEIGHTED = 0.1f;

    private GLImage GLImage;

    private GLText glText;

    private RectF srcRect, dstRect;

    private float x, y, rawX, rawY;

    public GLImageText(Context context, String text, float x, float y) {

        this.srcRect = srcRect;
        this.dstRect = srcRect;

        glText = new GLText(text, Until.dp2px(context.getResources().getDisplayMetrics().density, 20), x, y);

        float tX = x + glText.getWidth() * WEIGHTED;
        float tY = y + glText.getHeight() * WEIGHTED;

        glText.setLocation(tX, tY);

        float right = tX + glText.getWidth() + glText.getWidth() * WEIGHTED;
        float bottom = tY + glText.getHeight() + glText.getHeight() * WEIGHTED;

        GLImage = new GLImage(new RectF(x, y, right, bottom));
    }

    public void draw(float[] m) {

        GLImage.draw(m, 7);
        glText.draw(m);

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(float dstX, float dstY) {

        this.rawX = dstX + x;
        this.rawY = dstY + y;

        GLImage.setDstRect(rawX, rawY, rawX + GLImage.getSrcRect().width(), rawY + GLImage.getSrcRect().height());

        glText.setLocation(rawX + glText.getWidth() * WEIGHTED, rawY + glText.getHeight() * WEIGHTED);

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public RectF getSrcRect() {
        return GLImage.getSrcRect();
    }

    public RectF getDstRect() {
        return GLImage.getDstRect();
    }
}
