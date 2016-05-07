package com.kuo.kuoresume.until;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Kuo on 2016/4/18.
 */
public class Until {

    public static final int Color_SKY = Color.parseColor("#90CAF9");
    public static final int Color_Yellow = Color.parseColor("#FFEB3B");

    public static int dp2px(float density, int dp){
        return (int) Math.ceil(dp * density);
    }

    public static int px2dp(float density, int px) {
        return (int) Math.ceil(px / density);
    }

    public static int getTextWidth(Paint paint, String text) {

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        return rect.width();
    }

    public static int getTextHeight(Paint paint, String text) {

        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);

        return rect.height();
    }

    public static Bitmap createBitmap(Context context, int width, int height, int resId) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
