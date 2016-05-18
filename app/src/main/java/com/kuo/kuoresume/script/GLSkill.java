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
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLSkill extends ComputeRect{

    public static final int PLANT_FLOOR_SIZE = 3;
    public static final int BACKGROUND_FLOOR_SIZE = 5;

    public float OFFICE_COMPUTER_WIDTH = 192;
    public float OFFICE_COMPUTER_HEIGHT = 137;

    public float OFFICE_DOOR_WIDTH = 106;
    public float OFFICE_DOOR_HEIGHT = 200;

    public float SKILL_TEXT_SIZE = 150;

    private ArrayList<Image> grounds = new ArrayList<>();
    private ArrayList<GLSquare> squares = new ArrayList<>();

    private ArrayList<GLSquare> backgroundSquares = new ArrayList<>();

    private Image levelSign, signWood, officeComputer, leftDoor, rightDoor;

    private GLText signText, skillText;

    private GLChartRect glChartRect, languageChart;

    public GLImageText glImageText1, glImageText2;

    private GLSquare bg_yellow_square, bg_coffee_square;

    public GLSkill(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 30;

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height()
                + (int) viewComputeListener.getViewCompute().getPlantSize() * (PLANT_FLOOR_SIZE - 1);

        OFFICE_COMPUTER_WIDTH = OFFICE_COMPUTER_WIDTH * viewComputeListener.getScaling();
        OFFICE_COMPUTER_HEIGHT = OFFICE_COMPUTER_HEIGHT * viewComputeListener.getScaling();

        OFFICE_DOOR_WIDTH = OFFICE_DOOR_WIDTH * viewComputeListener.getScaling();
        OFFICE_DOOR_HEIGHT = OFFICE_DOOR_HEIGHT * viewComputeListener.getScaling();

        SKILL_TEXT_SIZE = SKILL_TEXT_SIZE * viewComputeListener.getScaling();

        createGrounds();
        createBackgroundSquares();
        createSquares();
        createImage();
        computeRect();
    }

    private void createBackgroundSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float[] color_coffee = new float[] {0.521568f, 0.172549f, 0, 1f};
        float[] color_yellow = new float[] {1f, 0.90588f, 0.439215f, 1f};

        for(int j = 0 ; j < BACKGROUND_FLOOR_SIZE ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom - plantSize * PLANT_FLOOR_SIZE + plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                if(j < 2)
                    backgroundSquares.add(new GLSquare(new RectF(left, top, right, bottom), color_coffee));
                else
                    backgroundSquares.add(new GLSquare(new RectF(left, top, right, bottom), color_yellow));

            }
        }

    }

    private void createSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float[] color = new float[] {0, 0, 0, 1f};

        for(int j = 0 ; j < PLANT_FLOOR_SIZE ; j++) {

            float left = 0;
            float top = height - plantSize - plantSize * j;
            float right = left + width;
            float bottom = top + plantSize;

            squares.add(new GLSquare(new RectF(left, top, right, bottom), color));
        }

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

        leftDoor = new Image(new RectF(0, plantHeight - OFFICE_DOOR_HEIGHT,
                OFFICE_DOOR_WIDTH, plantHeight));

        skillText = new GLText("Skill", (int) SKILL_TEXT_SIZE, 0, plantHeight - OFFICE_DOOR_HEIGHT * 1.2f);

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

        officeComputer = new Image(new RectF(plantSize * 7,
                plantHeight - OFFICE_COMPUTER_HEIGHT,
                plantSize * 7 + OFFICE_COMPUTER_WIDTH,
                plantHeight));

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

        float[] color_coffee = new float[] {0.7411764705882353f, 0.4823529411764706f, 0, 1f};
        float[] color_yellow = new float[] {1f, 0.8862745098039216f, 0.6784313725490196f, 1f};

        bg_yellow_square = new GLSquare(new RectF(0, 0, width, plantHeight), color_yellow);
        bg_coffee_square = new GLSquare(new RectF(0, plantHeight - plantSize * 2, width, plantHeight), color_coffee);
    }

    public void computeRect() {
        //computeGrounds();
        //computeBackgroundSquares();

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize * PLANT_FLOOR_SIZE;

        bg_yellow_square.computeDstRect(dstRect);
        bg_coffee_square.computeDstRect(dstRect);

        leftDoor.computeDstRect(dstRect);
        skillText.setLocation(dstRect.left, plantHeight - OFFICE_DOOR_HEIGHT * 1.2f);

        computeSquares();
        computeLevelSign();
        //computeSkill();
        computeSoftwareRect();
        computeLanguageRect();

        officeComputer.computeDstRect(dstRect);
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

    private void computeSquares() {

        for (GLSquare glSquare : squares) {
            glSquare.computeDstRect(dstRect);
        }

    }

    private void computeBackgroundSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int count = 0;
        for(int j = 0 ; j < BACKGROUND_FLOOR_SIZE ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom - plantSize * PLANT_FLOOR_SIZE - plantSize * j - plantSize;
                float right = left + plantSize;
                float bottom = top + plantSize;

                if(count == backgroundSquares.size())
                    continue;

                backgroundSquares.get(count).setDstRect(left, top, right, bottom);
                count ++;
            }
        }

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

    private void drawBackgroundSquares(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(GLSquare glSquare : backgroundSquares) {

            RectF rectF = glSquare.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                glSquare.draw(mvpMatrix);
            }
            count++;
        }
    }

    private void drawSquares(float[] mvpMatrix) {

        for(GLSquare glSquare : squares) {

            glSquare.draw(mvpMatrix);
        }
    }

    public void draw(float[] mvpMatrix) {


        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        if(dstRect.contains(contentRect.left, contentRect.top)
                || dstRect.contains(contentRect.left, contentRect.bottom)
                || dstRect.contains(contentRect.right, contentRect.top)
                || dstRect.contains(contentRect.right, contentRect.bottom)) {

            // Draw Background Square
            bg_yellow_square.draw(mvpMatrix);
            bg_coffee_square.draw(mvpMatrix);

            //drawGrounds(mvpMatrix);
            drawSquares(mvpMatrix);

            leftDoor.draw(mvpMatrix, 12);

            skillText.draw(mvpMatrix);

            officeComputer.draw(mvpMatrix, 10);

            //drawBackgroundSquares(mvpMatrix);

            //levelSign.draw(mvpMatrix, 3);
            //signText.draw(mvpMatrix);

            //signWood.draw(mvpMatrix, 6);

            glChartRect.draw(mvpMatrix);
            glImageText1.draw(mvpMatrix);

            languageChart.draw(mvpMatrix);
            glImageText2.draw(mvpMatrix);
        }

    }

}
