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
 * Created by Kuo on 2016/4/21.
 */
public class LevelSign extends BitmapRect {

    String text;

    Paint paint;

    public LevelSign(Context context, String text, RectF srcRect) {
        super(srcRect);

        this.text = text;

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "kgthelasttime.ttf"));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Until.dp2px(context.getResources().getDisplayMetrics().density, 35));
    }

    @Override
    public void draw(Canvas canvas, Bitmap bitmap) {

        if(spriteRect == null)
            spriteRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        canvas.drawBitmap(bitmap, spriteRect, dstRect, null);

        canvas.drawText(text, dstRect.left + dstRect.width() / 2, dstRect.top + dstRect.height() / 3, paint);

    }

}
