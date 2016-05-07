package com.kuo.kuoresume.object;

import android.graphics.RectF;

/**
 * Created by Kuo on 2016/5/6.
 */
public class GLImageText {

    private Image image;
    private GLText glText;

    private float x, y;
    private String text;

    public GLImageText(String text, float x, float y) {

        this.x = x;
        this.y = y;
        this.text = text;

        glText = new GLText(text, x, y);

        float width = glText.getWidth() * 1.3f;
        float height = glText.getHeight() * 1.3f;

        float left = x - (width - glText.getWidth()) / 2;
        float top = y - (height - glText.getHeight()) / 2;

        image = new Image(new RectF(left, top, left + width, top + height));
    }

    public void draw(float[] m) {

        image.draw(m, 7);
        glText.draw(m);

    }

    public void setLocation(float x, float y) {

        glText.setLocation(x, y);

        float left = x - (glText.getWidth() * 0.3f) / 2;
        float top = y - (glText.getHeight() * 0.3f) / 2;

        image.setDstRect(left, top, left + glText.getWidth() * 1.3f, top + glText.getHeight() * 1.3f);

    }

    public RectF getSrcRect() {
        return image.getSrcRect();
    }
}
