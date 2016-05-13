package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLChartRect;
import com.kuo.kuoresume.object.GLClouds;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLSkill extends ComputeRect{

    private ArrayList<Image> plants = new ArrayList<>();
    private ArrayList<Image> grounds = new ArrayList<>();

    private Image levelSign, signWood;

    private GLText signText, skillText;

    private GLChartRect glChartRect, languageChart;

    public GLImageText glImageText1, glImageText2;

    private GLClouds glClouds;

    private GLTrees glTrees;

    public GLSkill(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 35;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createGrounds();
        createImage();
        computeRect();
    }

    private void createGrounds() {

        RectF currentRect = viewComputeListener.getViewCompute().getCurRect();

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int groundVerticalSize = ((int) currentRect.height() - height) / (int) plantSize + 1;

        for(int j = 0 ; j < groundVerticalSize ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom + plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                grounds.add(new Image(new RectF(left, top, right, bottom)));
            }
        }

    }

    private void createPlants() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

            float left = dstRect.left + plantSize * i;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            plants.add(new Image(new RectF(left, top, right, bottom)));
        }
    }

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        levelSign = new Image(new RectF(0, plantHeight - plantSize * 4, plantSize * 4, plantHeight));

        signText = new GLText("Level 2", Until.dp2px(context.getResources().getDisplayMetrics().density, 20),
                0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4,  plantHeight - plantSize *2, plantSize * 6, plantHeight));

        skillText = new GLText("SKILL", Until.dp2px(context.getResources().getDisplayMetrics().density, 20),
                0, 0);


        String[] softwareAxisY = {"Android Studio", "Eclipse", "Git", "Unity"};
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

        glChartRect = new GLChartRect(context, 0, 0, softwareChartData);
        glChartRect.setPosition(plantSize * 8, plantHeight - glChartRect.getHeight());

        glImageText1 = new GLImageText(context, "SoftWare", plantSize * 8, plantHeight / 6);

        languageChart = new GLChartRect(context, 0, 0, languageChartData);
        languageChart.setPosition(plantSize * 10 + glChartRect.getWidth(), plantHeight - languageChart.getHeight());

        glImageText2= new GLImageText(context, "Language", plantSize * 8, plantHeight / 6);

        glClouds = new GLClouds(7, width, height);

        glTrees = new GLTrees(6, viewComputeListener.getScaling(), width, height);
    }

    public void draw(float[] mvpMatrix) {

        glTrees.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        glClouds.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        drawPlants(mvpMatrix);
        drawGrounds(mvpMatrix);

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        skillText.draw(mvpMatrix);

        glChartRect.draw(mvpMatrix);
        glImageText1.draw(mvpMatrix);

        languageChart.draw(mvpMatrix);
        glImageText2.draw(mvpMatrix);

    }

    private void drawGrounds(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();
        //int count = 0;
        for(Image image : grounds) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(mvpMatrix, 12);
        }
    }

    private void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();
        //int count = 0;
        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(mvpMatrix, 2);
        }
    }

    public void computeRect() {
        computeGrounds();
        computePlants();
        computeLevelSign();
        computeSkill();
        computeSoftwareRect();
        computeLanguageRect();
        glClouds.computeClouds(dstRect);
        glTrees.computeTrees(dstRect);
    }

    private void computeLevelSign() {

        RectF srcRect = levelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        levelSign.setDstRect(left, top, right, bottom);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + srcRect.height() / 4);
    }

    private void computeSkill() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        skillText.setLocation(signWood.getDstRect().centerX() - signText.getWidth() / 2,
                signWood.getDstRect().top + skillText.getHeight() / 2);

    }

    private void computeSoftwareRect() {


        float x = dstRect.left + glChartRect.getX();
        float y = dstRect.top + glChartRect.getY();

        glChartRect.setRawPosition(x, y);


        RectF textSrcRect = glImageText1.getSrcRect();

        glImageText1.setLocation(glChartRect.getRawCenterX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
    }

    private void computeLanguageRect() {


        float x = dstRect.left + languageChart.getX();
        float y = dstRect.top + languageChart.getY();

        languageChart.setRawPosition(x, y);

        RectF textSrcRect = glImageText2.getSrcRect();

        glImageText2.setLocation(languageChart.getRawCenterX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
    }

    private void computeGrounds() {

        RectF currentRect = viewComputeListener.getViewCompute().getCurRect();

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int groundVerticalSize = ((int) currentRect.height() - height) / (int) plantSize + 1;

        int count = 0;
        for(int j = 0 ; j < groundVerticalSize ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom + plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                if(count == grounds.size())
                    continue;

                grounds.get(count).setDstRect(left, top, right, bottom);
                count ++;
            }
        }
    }

    private void computePlants() {

        float plantSize = Until.dp2px(context.getResources().getDisplayMetrics().density, 50);

        int count = 0;
        for(Image image : plants) {

            float left = dstRect.left + plantSize * count;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            image.setDstRect(left, top, right, bottom);

            count++;
        }
    }
}
