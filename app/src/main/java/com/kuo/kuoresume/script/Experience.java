package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.object.BitmapRect;
import com.kuo.kuoresume.object.LevelSign;
import com.kuo.kuoresume.object.Stamp;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/4/24.
 */
public class Experience {

    private static final int RANGE_SIZE = 30;

    private int width, height;

    private float plantSize;

    private ObjectListener objectListener;

    private RectF dstRect = new RectF(0, 0, 0, 0);

    private LevelSign mLevelSign;

    private BitmapRect experience;

    private ArrayList<Stamp> stamps = new ArrayList<>();

    private String[] texts = {"急救小幫手", "藍球戰術版", "UrCoco", "MyBlog"};

    public Experience(Context context, ObjectListener objectListener) {

        this.objectListener = objectListener;
        this.plantSize = objectListener.getViewCompute().getPlantSize();
        this.width = (int) plantSize * RANGE_SIZE;
        this.height = (int) objectListener.getViewCompute().getContentRect().height() * 2;

        createObject(context);
        computePlant();
    }

    public void draw(Canvas canvas) {

        updateRect();

        experience.draw(canvas, objectListener.getHolderBitmap().exprience);
        mLevelSign.draw(canvas, objectListener.getHolderBitmap().signs);
        drawStamp(canvas);
        drawPlant(canvas);
    }

    private void drawStamp(Canvas canvas) {

        int count = 0;
        for(Stamp stamp : stamps) {
            stamp.draw(canvas, objectListener.getHolderBitmap().videoTape, objectListener.getHolderBitmap().plantSand, texts[count], objectListener.getViewCompute().getPlantSize());
            count++;
        }

    }

    private void createObject(Context context) {
        mLevelSign = new LevelSign(context, "Level 3", new RectF(0, 0, plantSize * 4, plantSize * 4));
        experience = new BitmapRect(new RectF(0, 0, plantSize * 5, plantSize * 3));

        createStamp(context);
    }

    private void createStamp(Context context) {

        float left = plantSize * 14;
        float right = plantSize * 17;

        for(int i = 0 ; i < texts.length ; i++) {

            left = i > 0 ? right + plantSize * 2 : left;
            right = left + plantSize * 3;

            Stamp stamp = new Stamp(context, new RectF(left, 0, right, plantSize * 5));
            stamps.add(stamp);
        }

    }

    ArrayList<RectF> plantRect = new ArrayList<>();

    private void computePlant() {

        for (int i = 0; i < RANGE_SIZE ; i++) {

            float left = plantSize * i;
            float top = height - plantSize;
            float right = left + plantSize;
            float bottom = height;

            RectF dstRect = new RectF(left, top, right, bottom);
            plantRect.add(dstRect);
        }
    }

    private void drawPlant(Canvas canvas) {

        RectF contentRect = objectListener.getViewCompute().getContentRect();

        Rect srcRect = new Rect(0, 0, objectListener.getHolderBitmap().plantSand.getWidth(), objectListener.getHolderBitmap().plantSand.getHeight());

        int count = 0;
        for(RectF rectF : plantRect) {

            float left = dstRect.left + plantSize * count;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            rectF.set(left, top, right, bottom);

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                canvas.drawBitmap(objectListener.getHolderBitmap().plantSand, srcRect, rectF, null);

            count++;
        }
    }

    private void updateRect() {
        updateLevelSignRect();
        updateExperienceRect();
        updateStampRect();
    }

    private void updateLevelSignRect() {

        RectF srcRect = mLevelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - srcRect.height();
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        mLevelSign.setDstRect(left, top, right, bottom);
    }

    private void updateExperienceRect() {

        RectF srcRect = experience.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - srcRect.height();
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        experience.setDstRect(left, top, right, bottom);
    }

    private void updateStampRect() {

        for(Stamp stamp : stamps) {
            RectF srcRect = stamp.getSrcRect();

            float left = dstRect.left + srcRect.left;
            float top = dstRect.bottom - plantSize - srcRect.height();
            float right = left + srcRect.width();
            float bottom = top + srcRect.height();

            stamp.setDstRect(left, top, right, bottom);
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
}
