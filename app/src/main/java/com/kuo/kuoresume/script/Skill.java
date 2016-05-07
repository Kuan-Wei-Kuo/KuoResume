package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.object.BitmapRect;
import com.kuo.kuoresume.object.ChartRect;
import com.kuo.kuoresume.object.LevelSign;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/4/23.
 */
public class Skill {

    private static final int RANG_SIZE = 35;

    private RectF dstRect = new RectF(0, 0, 0, 0);

    private Paint paint, testPaint;

    private int plantSize;

    private int width = 0, height = 0;

    private LevelSign mLevelSign;

    private BitmapRect mSkill;

    private ChartRect mSoftwareChart, mLanguageChart;

    private ObjectListener objectListener;

    private HolderBitmap holderBitmap;

    public Skill(Context context, ObjectListener objectListener) {

        testPaint = new Paint();
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(Until.dp2px(context.getResources().getDisplayMetrics().density, 10));
        testPaint.setColor(Color.BLUE);

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Until.dp2px(context.getResources().getDisplayMetrics().density, 20));

        plantSize =  Until.dp2px(context.getResources().getDisplayMetrics().density, 50);

        this.width = RANG_SIZE * plantSize;
        this.height = (int) objectListener.getViewCompute().getContentRect().height() * 2;

        this.objectListener = objectListener;

        holderBitmap = objectListener.getHolderBitmap();

        createObj(context);
        computePlant();
    }

    private void createObj(Context context) {

        mLevelSign = new LevelSign(context, "Level 2", new RectF(0, 0, plantSize * 4, plantSize * 4));
        mSkill = new BitmapRect(new RectF(plantSize * 4, 0, plantSize * 8, plantSize * 1));

        String[] softwareAxisY = {"AndroidStudio", "Eclipse", "Git", "Unity"};
        int[] values = {5, 3, 2, 2};

        String[] languageAxisY = {"Java", "C", "C#", "JavaScript"};
        int[] lvalues = {5, 3, 3, 2};

        ArrayList<ChartData> softwareChartData = new ArrayList<>();
        ArrayList<ChartData> languageChartData = new ArrayList<>();

        for(int i = 0 ; i < softwareAxisY.length ; i++) {

            ChartData chartData = new ChartData(softwareAxisY[i], values[i]);
            softwareChartData.add(chartData);

            ChartData chartData1 = new ChartData(languageAxisY[i], lvalues[i]);
            languageChartData.add(chartData1);

        }

        mSoftwareChart = new ChartRect(context, new RectF(plantSize * 9, 0, plantSize * 21, plantSize * 4),
                softwareChartData, objectListener);

        mLanguageChart = new ChartRect(context, new RectF(plantSize * 23, 0, plantSize * 35, plantSize * 4),
                languageChartData, objectListener);
    }

    public void draw(Canvas canvas) {

        ViewCompute viewCompute = objectListener.getViewCompute();

        updateLevelSignRect(viewCompute);
        updateSkillRect(viewCompute);
        updateLanguageRect();
        updateSoftwareChartRect();

        canvas.drawRect(dstRect, testPaint);
        mLevelSign.draw(canvas, holderBitmap.signs);
        mSkill.draw(canvas, holderBitmap.skill);
        mSoftwareChart.draw(canvas);
        mLanguageChart.draw(canvas);
        drawPlant(canvas);
    }

    ArrayList<RectF> plantRect = new ArrayList<>();

    private void computePlant() {

        for (int i = 0; i < RANG_SIZE; i++) {

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

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    private void updateLevelSignRect(ViewCompute viewCompute) {

        RectF currentRect = viewCompute.getCurRect();
        RectF mLevelSignRect = mLevelSign.getSrcRect();

        float left = dstRect.left + mLevelSignRect.left;
        float top = currentRect.top + height - plantSize - mLevelSignRect.height();
        float right = left + mLevelSignRect.width();
        float bottom = top + mLevelSignRect.height();

        mLevelSign.setDstRect(left, top, right, bottom);
    }

    private void updateSkillRect(ViewCompute viewCompute) {

        RectF currentRect = viewCompute.getCurRect();
        RectF mSkillRect = mSkill.getSrcRect();

        float left = dstRect.left + mSkillRect.left;
        float top = currentRect.top + height - plantSize - mSkillRect.height();
        float right = left + mSkillRect.width();
        float bottom = top + mSkillRect.height();

        mSkill.setDstRect(left, top, right, bottom);
    }

    private void updateLanguageRect() {

        RectF srcRect = mLanguageChart.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - mLanguageChart.getHeight();
        float right = left + mLanguageChart.getWidth();
        float bottom = dstRect.bottom - plantSize;

        mLanguageChart.setDstRect(left, top, right, bottom);
    }

    private void updateSoftwareChartRect() {

        RectF srcRect = mSoftwareChart.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.bottom - plantSize - mSoftwareChart.getHeight();
        float right = left + mSoftwareChart.getWidth();
        float bottom = dstRect.bottom - plantSize;

        mSoftwareChart.setDstRect(left, top, right, bottom);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
