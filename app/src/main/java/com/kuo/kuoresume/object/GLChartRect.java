package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ViewComputeListener;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLChartRect {

    public static final float WEIGTHED = 0.1f;

    public int IMAGE_SIZE = 70;

    public static final int MAX_X = 5;

    private ArrayList<RectF> axisYRect = new ArrayList<>();

    private ArrayList<ChartData> mChartData = new ArrayList<>();

    private ArrayList<Image> images = new ArrayList<>();

    private ArrayList<GLImageText> glImageTexts = new ArrayList<>();

    private float maxTextHeight = 0, maxTextWidth = 0, x = 0, y = 0, rawX = 0, rawY = 0;

    private int width, height, maxValue;

    private Context context;

    private ViewComputeListener viewComputeListener;

    public GLChartRect(Context context, float x, float y, ArrayList<ChartData> chartDatas, ViewComputeListener viewComputeListener) {

        this.context = context;

        this.x = x;
        this.y = y;

        this.mChartData = chartDatas;
        this.viewComputeListener = viewComputeListener;

        IMAGE_SIZE = IMAGE_SIZE * (int) viewComputeListener.getScaling();

        createGLImageTexts();
        createImage();
    }

    private void createImage() {

        int count = 0;
        for(ChartData chartData : mChartData) {

            for(int i = 0 ; i < chartData.getValue() ; i++) {

                float left = maxTextWidth + maxTextWidth * WEIGTHED + (IMAGE_SIZE + IMAGE_SIZE * WEIGTHED) * i;
                float top = (IMAGE_SIZE + IMAGE_SIZE * WEIGTHED) * count;
                float right = left + IMAGE_SIZE;
                float bottom = top + IMAGE_SIZE;

                Image image = new Image(new RectF(left, top, right, bottom));

                images.add(image);

                maxValue = Math.max(chartData.getValue(), maxValue);

                height = (int) Math.max(height, bottom);
            }
            count++;
        }

        width = (int) (maxTextWidth + maxTextWidth * WEIGTHED + IMAGE_SIZE * maxValue);
    }

    private void createGLImageTexts() {

        for(ChartData chartData : mChartData) {

            GLImageText glImageText = new GLImageText(context, chartData.getName(), 0, 0);

            glImageTexts.add(glImageText);

            maxTextWidth = Math.max(maxTextWidth, glImageText.getSrcRect().width());
            maxTextHeight = Math.max(maxTextHeight, glImageText.getSrcRect().height());
        }

    }

    public void draw(float[] m) {

        drawGLImageText(m);
        drawImage(m);

    }

    private void drawGLImageText(float[] m) {
        for (GLImageText glImageText : glImageTexts) {
            glImageText.draw(m);
        }
    }

    private void drawImage(float[] m) {

        for(Image image : images) {

            image.draw(m, 17);

        }
    }

    private void computeRect() {

        computeImageRect();
        computeGLImageTexts();

    }

    private void computeImageRect() {

        int count = 0;
        for(Image image : images) {

            float left = rawX + image.getSrcRect().left;
            float top = rawY + image.getSrcRect().top;
            float right = left + IMAGE_SIZE;
            float bottom = top + IMAGE_SIZE;

            image.setDstRect(left, top, right, bottom);

            count++;
        }

    }

    private void computeGLImageTexts() {

        int count = 0;
        for(GLImageText glImageText : glImageTexts) {

            float x = rawX + maxTextWidth - glImageText.getSrcRect().width();
            //float y = rawY + IMAGE_SIZE / 4 + (maxTextHeight + IMAGE_SIZE / 2) * count;
            float y = rawY + (IMAGE_SIZE + IMAGE_SIZE * WEIGTHED) * count + IMAGE_SIZE / 2 - glImageText.getSrcRect().height() / 2;

            glImageText.setLocation(x, y);

            count++;
        }
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setRawPosition(float x, float y) {

        rawX = x;
        rawY = y;

        computeRect();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getRawCenterX() {
        return rawX + width / 2;
    }

    public float getCenterY() {
        return y + height / 2;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
