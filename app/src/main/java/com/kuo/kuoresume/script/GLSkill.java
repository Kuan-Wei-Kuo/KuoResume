package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.Image;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/7.
 */
public class GLSkill extends ComputeRect{

    private ArrayList<Image> plants = new ArrayList<>();

    private Image levelSign, signWood;

    private GLText signText, skillText;

    public GLSkill(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 35;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createImage();
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

        signText = new GLText("Level 2", 0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4, plantHeight - plantSize * 2, plantSize * 6, plantHeight));

        skillText = new GLText("SKILL", 0, 0);
    }

    public void draw(float[] mvpMatrix) {

        drawPlants(mvpMatrix);

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        skillText.draw(mvpMatrix);
    }

    private void drawPlants(float[] mvpMatrix) {

        computePlants();

        //int count = 0;
        for(Image image : plants) {
            image.draw(mvpMatrix, 2);
        }
    }

    public void computeRect() {
        computeLevelSign();
        computeSkill();
    }

    private void computeLevelSign() {

        RectF srcRect = levelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        levelSign.setDstRect(left, top, right, bottom);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + srcRect.height() / 4);
    }

    private void computeSkill() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        skillText.setLocation(signWood.getDstRect().centerX() - signText.getWidth() / 2,
                signWood.getDstRect().top + skillText.getHeight() / 2);

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
