package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import com.kuo.kuoresume.compute.ImageDefaultSize;
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

    public static final int PLANT_FLOOR_SIZE = 3;

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

        height = (int) viewComputeListener.getViewCompute().getContentRect().height()
                + (int) viewComputeListener.getViewCompute().getPlantSize() * (PLANT_FLOOR_SIZE - 1);

        createGrounds();
        createImage();
        computeRect();
    }

    private void createGrounds() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int j = 0 ; j < PLANT_FLOOR_SIZE ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom + plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                grounds.add(new Image(new RectF(left, top, right, bottom)));
            }
        }

    }

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize * PLANT_FLOOR_SIZE;

        float scaling = viewComputeListener.getScaling();

        levelSign = new Image(new RectF(0,
                plantHeight - ImageDefaultSize.SIGN_HEIGHT * scaling,
                ImageDefaultSize.SIGN_WIDTH * scaling,
                plantHeight));

        String signString = "Level 2" +
                "";
        signText = new GLText(signString, (int) (ImageDefaultSize.SIGN_TEXT_SIZE * scaling),
                0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4,
                plantHeight - ImageDefaultSize.SIGN_WOOD_HEIGHT * scaling,
                plantSize * 4 + ImageDefaultSize.SIGN_WOOD_WIDTH * scaling,
                plantHeight));

        skillText = new GLText("Skill",(int) (ImageDefaultSize.SIGN_WOOD_TEXT_SIZE * scaling),
                0, 0);


        String[] softwareAxisY = {"Android Studio", "Eclipse", "Git", "Unity"};
        int[] values = {5, 3, 2, 2};

        String[] languageAxisY = {"Java", "C", "C Sharp", "JavaScript"};
        int[] lvalues = {5, 3, 3, 2};

        ArrayList<ChartData> softwareChartData = new ArrayList<>();
        ArrayList<ChartData> languageChartData = new ArrayList<>();

        for(int i = 0 ; i < softwareAxisY.length ; i++) {

            ChartData chartData = new ChartData(softwareAxisY[i], values[i]);
            softwareChartData.add(chartData);

            ChartData chartData1 = new ChartData(languageAxisY[i], lvalues[i]);
            languageChartData.add(chartData1);

        }

        glChartRect = new GLChartRect(context, 0, 0, softwareChartData, viewComputeListener);
        glChartRect.setPosition(plantSize * 8, plantHeight - glChartRect.getHeight());

        glImageText1 = new GLImageText(context, "SoftWare", plantSize * 8, plantHeight / 6);

        languageChart = new GLChartRect(context, 0, 0, languageChartData, viewComputeListener);
        languageChart.setPosition(plantSize * 10 + glChartRect.getWidth(), plantHeight - languageChart.getHeight());

        glImageText2= new GLImageText(context, "Language", plantSize * 8, plantHeight / 6);

        glClouds = new GLClouds(7, width, height);

        glTrees = new GLTrees(6, viewComputeListener.getScaling(), width, (int) (plantHeight + plantSize));
    }

    public void computeRect() {
        computeGrounds();
        computeLevelSign();
        computeSkill();
        computeSoftwareRect();
        computeLanguageRect();
        glClouds.computeClouds(dstRect);
        glTrees.computeTrees(dstRect);
    }

    private void computeLevelSign() {

        levelSign.computeDstRect(dstRect);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + signText.getHeight());
    }

    private void computeSkill() {

        signWood.computeDstRect(dstRect);

        skillText.setLocation(signWood.getDstRect().centerX() - skillText.getWidth() / 2,
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

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int count = 0;
        for(int j = 0 ; j < PLANT_FLOOR_SIZE ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom - plantSize - plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                if(count == grounds.size())
                    continue;

                grounds.get(count).setDstRect(left, top, right, bottom);
                count ++;
            }
        }

    }

    private void drawGrounds(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(Image image : grounds) {

            RectF rectF = image.getDstRect();

            int row = count / PLANT_RANGE_SIZE;

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                if (row == PLANT_FLOOR_SIZE - 1)
                    image.draw(mvpMatrix, 2);
                else
                    image.draw(mvpMatrix, 12);
            }
            count++;
        }
    }

    public void draw(float[] mvpMatrix) {

        glTrees.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        glClouds.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
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
}
