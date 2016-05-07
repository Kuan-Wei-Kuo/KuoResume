package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/*
 * Created by Kuo on 2016/4/23.
 */
public class ChartRect extends BitmapRect{

    private static final int MAX_X = 5;
    //private static final int MIN_X = 0;

    private static final float LAYOUT_WEIGHTED = 1.2f;

    private ArrayList<RectF> axisYRect = new ArrayList<>();

    private ArrayList<ChartData> mChartData = new ArrayList<>();

    private ObjectListener objectListener;

    private Rect signSrcRect;

    private Paint paint;

    private float textHeight = 0, textWidth = 0;

    private int height, width;

    private float columnHeightMargin, columnWidthMargin;

    public ChartRect(Context context, RectF srcRect, ArrayList<ChartData> chartData, ObjectListener objectListener) {
        super(srcRect);

        this.mChartData = chartData;
        this.objectListener = objectListener;

        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(Typeface.createFromAsset(context.getAssets(), "kgthelasttime.ttf"));
        paint.setTextSize(Until.dp2px(context.getResources().getDisplayMetrics().density, 20));
        paint.setColor(Color.WHITE);

        //columnHeight = srcRect.height() * 0.8f / MAX_X;
        columnHeightMargin = srcRect.height() * 0.2f / MAX_X;

        //columnWidth = srcRect.width() * 0.95f / MAX_X;
        columnWidthMargin = srcRect.width() * 0.05f / MAX_X;

        computeTextLayout();
        createRect();
    }

    public void draw(Canvas canvas) {
        computeRect();

        drawAxisY(canvas);
        drawCircle(canvas);
        drawGraph(canvas);
    }

    private void drawAxisY(Canvas canvas) {

        int count = 0;
        for(RectF rectF : axisYRect) {

            if(signSrcRect == null)
                signSrcRect = new Rect(0, 0, objectListener.getHolderBitmap().pinkTag.getWidth(), objectListener.getHolderBitmap().pinkTag.getHeight());

            canvas.drawBitmap(objectListener.getHolderBitmap().pinkTag, signSrcRect, rectF, null);
            canvas.drawText(mChartData.get(count).getName(), rectF.centerX(), rectF.centerY() + textHeight * LAYOUT_WEIGHTED / 4, paint);

            count++;
        }

    }

    private void drawGraph(Canvas canvas) {

        int mapWidth = objectListener.getHolderBitmap().spiderHead.getWidth();

        int count = 0;
        for(ChartData chartData : mChartData) {

            for(int i = 0 ; i < chartData.getValue() ; i++) {

                float left = dstRect.left + textWidth * LAYOUT_WEIGHTED + columnWidthMargin + (columnWidthMargin + mapWidth) *  i;
                float top = axisYRect.get(count).centerY() - mapWidth / 2;

                canvas.drawBitmap(objectListener.getHolderBitmap().spiderHead, left, top, null);
            }

            count++;
        }

    }

    private void drawCircle(Canvas canvas) {

        int mapWidth = objectListener.getHolderBitmap().spiderHead.getWidth();

        for(RectF rectF : axisYRect) {

            for(int i = 0 ; i < MAX_X ; i++) {

                float left = dstRect.left + textWidth * LAYOUT_WEIGHTED + columnWidthMargin + (columnWidthMargin + mapWidth) *  i;

                canvas.drawCircle(left + mapWidth / 2, rectF.centerY(), mapWidth / 2, paint);
            }
        }
    }

    private void createRect() {

        int count = 0;
        for(ChartData chartData : mChartData) {

            float left = dstRect.left;
            float right = left + Until.getTextWidth(paint, chartData.getName()) * LAYOUT_WEIGHTED;
            float top = dstRect.top + (textHeight * LAYOUT_WEIGHTED + columnHeightMargin) * count;
            float bottom = top + textHeight * LAYOUT_WEIGHTED;

            axisYRect.add(new RectF(left, top, right, bottom));

            count++;
        }
    }

    private void computeTextLayout() {

        for(ChartData chartData : mChartData) {
            textWidth = Math.max(textWidth, Until.getTextWidth(paint, chartData.getName()));
            textHeight = Math.max(textHeight, Until.getTextHeight(paint, chartData.getName()));
        }

        int mapWidth = objectListener.getHolderBitmap().spiderHead.getWidth();
        int mapHeight = objectListener.getHolderBitmap().spiderHead.getHeight();

        float weightedHeight = textHeight * LAYOUT_WEIGHTED;

        height = (int) ((weightedHeight + columnHeightMargin + mapHeight - weightedHeight)) * mChartData.size();
        width =  (int) (textWidth * LAYOUT_WEIGHTED + columnWidthMargin) + ((int) columnWidthMargin + mapWidth) *  MAX_X;

    }

    private void computeRect() {

        int mapHeight = objectListener.getHolderBitmap().spiderHead.getHeight();

        float weightedHeight = textHeight * LAYOUT_WEIGHTED;
        float weightedWidth = textWidth * LAYOUT_WEIGHTED;

        int count = 0;
        for(RectF rectF : axisYRect) {

            float width = Until.getTextWidth(paint, mChartData.get(count).getName());

            float left = width == textWidth ? dstRect.left : dstRect.left + weightedWidth - width * LAYOUT_WEIGHTED;
            float right = left + width * LAYOUT_WEIGHTED;
            float top = dstRect.top + (weightedHeight + columnHeightMargin + mapHeight - weightedHeight) * count;
            float bottom = top + weightedHeight;

            rectF.set(left, top, right, bottom);

            count++;

        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
