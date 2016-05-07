package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.script.ComputeRect;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLChartRect extends ComputeRect {

    private static final int MAX_X = 5;

    private ArrayList<RectF> axisYRect = new ArrayList<>();

    private ArrayList<ChartData> mChartData = new ArrayList<>();

    private ArrayList<Image> images = new ArrayList<>();

    private ArrayList<GLImageText> glImageTexts = new ArrayList<>();

    private float maxTextHeight = 0, maxTextWidth = 0;

    public GLChartRect(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        createGLImageTexts();

    }

    private void createGLImageTexts() {

        for(ChartData chartData : mChartData) {

            GLImageText glImageText = new GLImageText(chartData.getName(), 0, 0);

            glImageTexts.add(glImageText);

            maxTextWidth = Math.max(maxTextWidth, glImageText.getSrcRect().width());
            maxTextHeight = Math.max(maxTextHeight, glImageText.getSrcRect().height());
        }
    }

    public void draw(float[] m) {

        drawGLImageText(m);

    }

    private void drawGLImageText(float[] m) {
        for (GLImageText glImageText : glImageTexts) {
            glImageText.draw(m);
        }
    }

    public void computeRect() {

        computeGLImageTexts();

    }

    private void computeGLImageTexts() {

        int count = 0;
        for(GLImageText glImageText : glImageTexts) {

            float x = dstRect.left;
            float y = maxTextHeight * count;

            glImageText.setLocation(x, y);

            count++;
        }
    }

}
