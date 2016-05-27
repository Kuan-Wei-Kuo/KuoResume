package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.data.ChartData;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLChartRect;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLImage;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLSkill extends ComputeRect{

    private static final float OBJECT_384PX_UV_BOX_WIDTH = 0.25f;

    private static final float OBJECT_384PX_SIZE = 384;

    public static final int PLANT_FLOOR_SIZE = 3;
    public static final int BACKGROUND_FLOOR_SIZE = 5;

    public float OFFICE_COMPUTER_WIDTH = 192;
    public float OFFICE_COMPUTER_HEIGHT = 137;

    public float OFFICE_DOOR_WIDTH = 106;
    public float OFFICE_DOOR_HEIGHT = 200;

    private float OFFICE_WIDTH = 400;
    private float OFFICE_HEIGHT = 300;

    public float SKILL_TEXT_SIZE = 150;

    private ArrayList<GLImage> grounds = new ArrayList<>();
    private ArrayList<GLSquare> squares = new ArrayList<>();

    private GLSquare floorSquare;

    private GLImage levelSign, signWood, officeComputer, softwareComputer, languageComputer, skillPhone;

    private GLText signText, skillText;

    private GLChartRect glChartRect, languageChart;

    public GLImageText glImageText1, glImageText2;

    private GLSquare bg_yellow_square, bg_coffee_square;

    public GLSkill(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 40;

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height()
                + (int) viewComputeListener.getViewCompute().getPlantSize() * (PLANT_FLOOR_SIZE - 1);

        OFFICE_COMPUTER_WIDTH = OFFICE_COMPUTER_WIDTH * viewComputeListener.getScaling();
        OFFICE_COMPUTER_HEIGHT = OFFICE_COMPUTER_HEIGHT * viewComputeListener.getScaling();

        OFFICE_DOOR_WIDTH = OFFICE_DOOR_WIDTH * viewComputeListener.getScaling();
        OFFICE_DOOR_HEIGHT = OFFICE_DOOR_HEIGHT * viewComputeListener.getScaling();

        OFFICE_WIDTH = OFFICE_WIDTH * viewComputeListener.getScaling();
        OFFICE_HEIGHT = OFFICE_HEIGHT * viewComputeListener.getScaling();

        SKILL_TEXT_SIZE = SKILL_TEXT_SIZE * viewComputeListener.getScaling();

        createGrounds();
        createSquares();
        createImage();
        computeRect();
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

        floorSquare = new GLSquare(new RectF(0, height - plantSize * (PLANT_FLOOR_SIZE - 1), width, height), color);
        floorSquare.setColliderListener(viewComputeListener.getGLCharacter().getCurrentJumpListener());

    }

    private void createGrounds() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int j = 0 ; j < PLANT_FLOOR_SIZE ; j++) {
            for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

                float left = dstRect.left + plantSize * i;
                float top = dstRect.bottom + plantSize * j;
                float right = left + plantSize;
                float bottom = top + plantSize;

                grounds.add(new GLImage(new RectF(left, top, right, bottom)));
            }
        }
    }

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize * PLANT_FLOOR_SIZE;

        float scaling = viewComputeListener.getScaling();

        skillText = new GLText("Skill", (int) SKILL_TEXT_SIZE, 0, plantHeight - OFFICE_DOOR_HEIGHT * 1.2f);

        skillPhone = new GLImage(new RectF(0, plantHeight - OBJECT_384PX_SIZE * scaling,
                OBJECT_384PX_SIZE * scaling, plantHeight));

        skillPhone.setUVS(new float[] {
                1 - OBJECT_384PX_UV_BOX_WIDTH, 0,
                1 - OBJECT_384PX_UV_BOX_WIDTH, 1,
                1, 1,
                1, 0
        });

        levelSign = new GLImage(new RectF(0,
                plantHeight - ImageDefaultSize.SIGN_HEIGHT * scaling,
                ImageDefaultSize.SIGN_WIDTH * scaling,
                plantHeight));

        String signString = "Level 2" +
                "";
        signText = new GLText(signString, (int) (ImageDefaultSize.SIGN_TEXT_SIZE * scaling),
                0, plantHeight - plantSize * 4);

        signWood = new GLImage(new RectF(plantSize * 4,
                plantHeight - ImageDefaultSize.SIGN_WOOD_HEIGHT * scaling,
                plantSize * 4 + ImageDefaultSize.SIGN_WOOD_WIDTH * scaling,
                plantHeight));

        officeComputer = new GLImage(new RectF(plantSize * 7,
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
        glChartRect.setPosition(plantSize * 9, glChartRect.getHeight() * 0.1f);

        glImageText1 = new GLImageText(context, "SoftWare", plantSize * 8, plantHeight / 6);

        softwareComputer = new GLImage(new RectF(plantSize * 8, 0, plantSize * 8 + glChartRect.getWidth() + plantSize, plantHeight));

        languageChart = new GLChartRect(context, 0, 0, languageChartData, viewComputeListener);
        languageChart.setPosition(softwareComputer.getSrcRect().right + plantSize * 1.5f, languageChart.getHeight() * 0.1f);

        languageComputer = new GLImage(new RectF(softwareComputer.getSrcRect().right + plantSize, 0, softwareComputer.getSrcRect().right + plantSize + languageChart.getWidth() + plantSize, plantHeight));

        glImageText2= new GLImageText(context, "Language", plantSize * 8, plantHeight / 6);

        float[] color_coffee = new float[] {0.7411764705882353f, 0.4823529411764706f, 0, 1f};
        float[] color_yellow = new float[] {1f, 0.8862745098039216f, 0.6784313725490196f, 1f};

        bg_yellow_square = new GLSquare(new RectF(0, 0, width, plantHeight), color_yellow);
        bg_coffee_square = new GLSquare(new RectF(0, plantHeight - plantSize * 2, width, plantHeight), color_coffee);
    }

    public void computeRect() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize * PLANT_FLOOR_SIZE;

        bg_yellow_square.computeDstRect(dstRect);
        bg_coffee_square.computeDstRect(dstRect);

        skillText.setLocation(dstRect.left, plantHeight - OFFICE_DOOR_HEIGHT * 1.2f);

        floorSquare.computeDstRect(dstRect);
        floorSquare.startCollider(viewComputeListener.getGLCharacter().getDstRect());

        computeSquares();
        computeLevelSign();
        computeSoftwareRect();
        computeLanguageRect();

        skillPhone.computeDstRect(dstRect);
        softwareComputer.computeDstRect(dstRect);
        languageComputer.computeDstRect(dstRect);
        officeComputer.computeDstRect(dstRect);
    }

    private void computeLevelSign() {

        levelSign.computeDstRect(dstRect);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + signText.getHeight());
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

            drawSquares(mvpMatrix);

            skillPhone.draw(mvpMatrix, 16);

            softwareComputer.draw(mvpMatrix, 28);
            glChartRect.draw(mvpMatrix);
            //glImageText1.draw(mvpMatrix);

            languageComputer.draw(mvpMatrix, 28);
            languageChart.draw(mvpMatrix);
            //glImageText2.draw(mvpMatrix);
        }

    }

}
