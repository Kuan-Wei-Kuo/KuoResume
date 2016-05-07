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
 * Created by Kuo on 2016/4/29.
 */
public class Stamp extends BitmapRect {

    private Rect spriteSrcRect;

    private RectF imageDstRect = new RectF(0, 0, 0, 0);

    private Paint paint;

    public Stamp(Context context, RectF srcRect) {
        super(srcRect);

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "kgthelasttime.ttf"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Until.dp2px(context.getResources().getDisplayMetrics().density, 18));
    }

    public void draw(Canvas canvas, Bitmap stamp, Bitmap image, String text, float plantSize) {


        if(spriteSrcRect == null)
            spriteSrcRect = new Rect(0, 0, stamp.getWidth(), stamp.getHeight());

        imageDstRect.set(dstRect.left + dstRect.width() / 4,
                dstRect.top + dstRect.height() / 2,
                dstRect.right - dstRect.width() / 4,
                dstRect.bottom - dstRect.height() / 8);

        canvas.drawBitmap(stamp, spriteRect, dstRect, null);
        canvas.drawBitmap(image, spriteRect, imageDstRect, null);

        canvas.drawText(text, dstRect.centerX(), imageDstRect.bottom, paint);

    }
}
