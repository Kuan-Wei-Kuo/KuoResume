package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.until.Until;

/**
 * Created by Kuo on 2016/5/6.
 */
public class GLImageText {

    public static final float WEIDTHED = 0.1f;

    private Image image;
    private GLText glText;

    private float x, y, rawX, rawY;

    public GLImageText(Context context, String text, float x, float y) {

        glText = new GLText(text, Until.dp2px(context.getResources().getDisplayMetrics().density, 20),
                x, y);

        float tX = x + glText.getWidth() * WEIDTHED;
        float tY = y + glText.getHeight() * WEIDTHED;

        glText.setLocation(tX, tY);

        float right = tX + glText.getWidth() + glText.getWidth() * WEIDTHED;
        float bottom = tY + glText.getHeight() + glText.getHeight() * WEIDTHED;

        image = new Image(new RectF(x, y, right, bottom));
    }

    public void draw(float[] m) {

        image.draw(m, 7);
        glText.draw(m);

    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(float x, float y) {

        this.rawX = x;
        this.rawY = y;

        image.setDstRect(x, y, x + image.getSrcRect().width(), y + image.getSrcRect().height());

        glText.setLocation(x + glText.getWidth() * WEIDTHED, y + glText.getHeight() * WEIDTHED);

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public RectF getSrcRect() {
        return image.getSrcRect();
    }
}
