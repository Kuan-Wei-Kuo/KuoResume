package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.kuo.kuoresume.until.Until;

/**
 * Created by Kuo on 2016/5/2.
 */
public class TalkWindow{

    private Paint paint;

    private String text;

    protected RectF srcRect;

    protected RectF dstRect = new RectF(0, 0, 0, 0);

    protected int width = 0, height = 0, count = 0;

    public TalkWindow(Context context, String text, int textSize) {

        this.text = text;

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "kgthelasttime.ttf"));

        computeText();
    }

    private void computeText() {
        for (String line: text.split("\n")) {
            width = Math.max(Until.getTextWidth(paint, line), width);
            count++;
        }
        height = Until.getTextHeight(paint, text);
    }

    public void draw(Canvas canvas) {

        float x = dstRect.centerX(), y = dstRect.top + height;

        for (String line: text.split("\n")) {
            canvas.drawText(line, x, y, paint);
            y += paint.descent() - paint.ascent();
        }
    }

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCount() {
        return count;
    }
}
