package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.animation.SpriteAnimation;

/*
 * Created by Kuo on 2016/4/20.
 */
public class MusicMan extends BitmapRect implements SpriteAnimation.OnUpdateListener{

    private SpriteAnimation spriteAnimation;

    private Bitmap musicMan;

    public MusicMan(Context context, RectF srcRect) {
        super(srcRect);

        musicMan = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.char_music),
                (int) srcRect.width() * 16, (int) srcRect.height(), true);

        spriteRect = new Rect(0, 0, 0 ,0);

        spriteAnimation = new SpriteAnimation(100, new Rect(0, 0, musicMan.getWidth(), musicMan.getHeight()), (int) srcRect.width(),  (int) srcRect.height(), 16);
        spriteAnimation.setOnUpdateListener(this);
    }

    public void draw(Canvas canvas) {
        spriteAnimation.start();
        canvas.drawBitmap(musicMan, spriteRect, dstRect, null);
    }

    @Override
    public void onUpdate(int currentHorizontalFrame, int currentVerticalFrame) {

        int left = currentHorizontalFrame * spriteAnimation.getFrameWidth();
        int right = left + spriteAnimation.getFrameWidth();
        int top = currentVerticalFrame * spriteAnimation.getFrameHeight();
        int bottom = top + spriteAnimation.getFrameHeight();

        spriteRect.set(left, top, right, bottom);
    }

    @Override
    public void end() {

    }
}
