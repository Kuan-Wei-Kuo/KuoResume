package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.kuo.kuoresume.until.Until;

/**
 * Created by Kuo on 2016/4/20.
 */
public class BitmapText extends BitmapRect{

    private String text;

    private Paint paint;

    private Rect spriteRect;

    private float textHeight;

    public BitmapText(Context context, String text, RectF srcRect) {
        super(srcRect);

        this.text = text;

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "kgthelasttime.ttf"));
        paint.setTextSize(Until.dp2px(context.getResources().getDisplayMetrics().density, 20));
        paint.setColor(Color.WHITE);

        textHeight = Until.getTextHeight(paint, text) * 1.2f;

        srcRect.left = srcRect.centerX() - Until.getTextWidth(paint, text) / 1.5f;
        srcRect.right = srcRect.centerX() + Until.getTextWidth(paint, text) / 1.5f;
        srcRect.bottom = textHeight * 1.2f;
    }

    public void draw(Canvas canvas, Bitmap bitmap) {

        if(spriteRect == null)
            spriteRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, spriteRect, dstRect, null);

        canvas.drawText(text, dstRect.centerX(), dstRect.centerY() + textHeight / 4, paint);

    }

    public float getHeight() {
        return srcRect.height();
    }
}
