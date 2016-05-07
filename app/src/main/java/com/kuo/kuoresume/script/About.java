package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.object.BitmapRect;
import com.kuo.kuoresume.object.BitmapText;
import com.kuo.kuoresume.object.LevelSign;
import com.kuo.kuoresume.object.MusicMan;
import com.kuo.kuoresume.object.Stage;
import com.kuo.kuoresume.object.TalkWindow;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/*
 * Created by Kuo on 2016/4/18.
 */
public class About {

    private static final int RANGE_SIZE = 27;

    private static final int SEA_MIN_SIZE = 14;
    private static final int SEA_MAX_SIZE = 17;

    private TalkWindow talkWindow;

    RectF dstRect = new RectF(0, 0, 0, 0);

    Bitmap sea, bat;

    Paint testPaint;

    int width = 0, height = 0;

    Context context;

    ObjectListener objectListener;

    float plantSize;

    public About(Context context, ObjectListener objectListener) {

        this.context = context;
        this.objectListener = objectListener;
        this.plantSize = objectListener.getViewCompute().getPlantSize();
        this.width = RANGE_SIZE * (int) plantSize;
        this.height = (int) objectListener.getViewCompute().getContentRect().height();

        testPaint = new Paint();
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(Until.dp2px(context.getResources().getDisplayMetrics().density, 10));
        testPaint.setColor(Color.RED);

        prepareBitmap(context);

        talkWindow = new TalkWindow(context,
                "Oh! I fly up! \n" +
                        "Lying to you.",
                Until.dp2px(context.getResources().getDisplayMetrics().density, 20));

        computePlant();
    }

    public void drawAbout(Canvas canvas, ViewCompute viewCompute, HolderBitmap holderBitmap, Human human) {

        prepareRect();

        mLevelSign.draw(canvas, holderBitmap.signs);
        mAbout.draw(canvas, holderBitmap.about);
        mTicketStation.draw(canvas, holderBitmap.ticketStation);
        mBuild85.draw(canvas, holderBitmap.build85);
        bitmapText.draw(canvas, holderBitmap.woodTag);
        drawPlant(canvas);
        drawGround(canvas);
        canvas.drawRect(dstRect, testPaint);

        talkWindow.setDstRect(human.dstRect.right,
                human.dstRect.top - talkWindow.getHeight() * talkWindow.getCount(),
                human.dstRect.right + talkWindow.getWidth(),
                human.dstRect.top);

        talkWindow.draw(canvas);

        if(human.dstRect.centerX() >= dstRect.left + viewCompute.getPlantSize() * SEA_MIN_SIZE && human.dstRect.centerX() <= dstRect.left + viewCompute.getPlantSize() * SEA_MAX_SIZE + viewCompute.getPlantSize()) {
            human.update(Human.HUMAN_BAT);
        } else {
            if(human.getState() == Human.HUMAN_BAT)
                human.setState(Human.HUMAN_IDLE);
        }

        if(human.dstRect.left > dstRect.right && viewCompute.getCurRect().bottom > viewCompute.getContentRect().bottom) {
            human.update(Human.HUMAN_DOWN);
        } else if(human.dstRect.left < dstRect.right && viewCompute.getCurRect().top < viewCompute.getContentRect().top) {
            human.update(Human.HUMAN_UP);
        } else {
            if(human.getState() == Human.HUMAN_DOWN)
                human.setState(Human.HUMAN_IDLE);
        }

        stageObj.draw(canvas);
        musicManObj.draw(canvas);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    private Stage stageObj;
    private MusicMan musicManObj;
    private LevelSign mLevelSign;
    private BitmapRect mTicketStation, mBuild85, mAbout;
    private BitmapText bitmapText;

    private void prepareBitmap(Context context) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        int width = Until.dp2px(context.getResources().getDisplayMetrics().density, 50);
        int height = width;

        sea = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.sea),
                width, height, true);
        bat = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bat),
                width, height, true);

        stageObj = new Stage(context, width, height / 2);

        mLevelSign = new LevelSign(context, "Level 1", new RectF(0, 0, plantSize * 4, plantSize * 4));

        mAbout = new BitmapRect(new RectF(plantSize * 4, 0, plantSize * 8, plantSize * 2));

        mTicketStation = new BitmapRect(new RectF(plantSize * 11, 0, plantSize * 13, plantSize * 2));

        mBuild85 = new BitmapRect(new RectF(plantSize * 14, 0, plantSize * 14 + plantSize * 2, plantSize * 4));

        bitmapText = new BitmapText(context, "Live in Kaohsiung City", new RectF(plantSize * 14, 0, plantSize * 16, 0));

        musicManObj = new MusicMan(context, new RectF(plantSize * 20, 0, plantSize * 22, plantSize * 2));

    }

    private void prepareRect() {
        updateLevelSignRect();
        updateTicketStationRect();
        updateBuild85Rect();
        updateAboutRect();
        updateMusicManRect();
        updateTextRect();
    }

    private void updateMusicManRect() {

        RectF srcRect = musicManObj.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - srcRect.height();
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        musicManObj.setDstRect(left, top, right, bottom);

    }

    private void updateLevelSignRect() {

        RectF mLevelSignRect = mLevelSign.getSrcRect();

        float left = dstRect.left + mLevelSignRect.left;
        float top = height - plantSize - mLevelSignRect.height();
        float right = left + mLevelSignRect.width();
        float bottom = top + mLevelSignRect.height();

        mLevelSign.setDstRect(left, top, right, bottom);
    }

    private void updateTicketStationRect() {

        RectF mTicketStationRect = mTicketStation.getSrcRect();

        float left = dstRect.left + mTicketStationRect.left;
        float top = height - plantSize - mTicketStationRect.height();
        float right = left + mTicketStationRect.width();
        float bottom = top + mTicketStationRect.height();

        mTicketStation.setDstRect(left, top, right, bottom);
    }

    private void updateBuild85Rect() {

        RectF srcRect = mBuild85.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - srcRect.height();
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        mBuild85.setDstRect(left, top, right, bottom);
    }

    private void updateAboutRect() {

        RectF srcRect = mAbout.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = height - plantSize - srcRect.height() + 30;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        mAbout.setDstRect(left, top, right, bottom);
    }

    private void updateTextRect() {

        RectF srcRect = bitmapText.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + bitmapText.getHeight() * 2;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        bitmapText.setDstRect(left, top, right, bottom);
    }

    ArrayList<RectF> plantRects = new ArrayList<>();

    private void computePlant() {

        for (int i = 0; i < RANGE_SIZE; i++) {

            float left = plantSize * i;
            float top = height - plantSize;
            float right = left + plantSize;
            float bottom = height;

            RectF dstRect = new RectF(left, top, right, bottom);
            plantRects.add(dstRect);
        }
    }

    private void drawPlant(Canvas canvas) {

        RectF contentRect = objectListener.getViewCompute().getContentRect();

        Rect srcRect = new Rect(0, 0, objectListener.getHolderBitmap().plantSand.getWidth(), objectListener.getHolderBitmap().plantSand.getHeight());

        int count = 0;
        for(RectF rectF : plantRects) {

            float left = dstRect.left + plantSize * count;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            rectF.set(left, top, right, bottom);

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                if (count >= SEA_MIN_SIZE && count <= SEA_MAX_SIZE)
                    canvas.drawBitmap(objectListener.getHolderBitmap().plantSea, srcRect, rectF, null);
                else
                    canvas.drawBitmap(objectListener.getHolderBitmap().plantSand, srcRect, rectF, null);
            }

            count++;
        }
    }

    private void drawGround(Canvas canvas) {

        RectF currentRect = objectListener.getViewCompute().getCurRect();

        int groundVerticalSize = ((int) currentRect.height() - height) / (int) plantSize + 1;

        for(int i = 0 ; i < groundVerticalSize ; i++) {

            for(int j = 0 ; j < RANGE_SIZE ; j++) {

                float left = dstRect.left + plantSize * j;
                float top = dstRect.bottom + plantSize * i;

                canvas.drawBitmap(objectListener.getHolderBitmap().sand, left, top, null);
            }
        }

    }
}
