package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.R;

/**
 * Created by Kuo on 2016/4/20.
 */
public class Stage {

    private Bitmap stage;

    private Rect srcRect;

    private RectF dstRect;

    public Stage(Context context, int width, int height) {

        stage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.stage),
                width, height, true);

        srcRect = new Rect(0, 0, stage.getWidth(), stage.getHeight());
        dstRect = new RectF(0, 0, 0 ,0);
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(stage, srcRect, dstRect, null);
    }

    public void setDstRect(RectF dstRect) {
        this.dstRect = dstRect;
    }

    public int getHeight() {
        return srcRect.height();
    }
}
