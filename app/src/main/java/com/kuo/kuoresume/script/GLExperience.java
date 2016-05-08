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
 * Created by User on 2016/5/8.
 */
public class GLExperience extends ComputeRect {

    private static final int VIDEO_TAPES_SIZE = 350;

    private ArrayList<Image> plants = new ArrayList<>();

    private Image levelSign, signWood;

    private GLText signText, experience;

    private ArrayList<Image> videoTapes = new ArrayList<>();

    private String[] texts = {"MyChartLib", "急救小幫手", "籃球戰術版", "UrCoco", "MyBlog"};

    public GLExperience(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 30;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createImage();
        createVideoTapes();
    }

    private void createImage() {


        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        levelSign = new Image(new RectF(0, plantHeight - plantSize * 4, plantSize * 4, plantHeight));

        signText = new GLText("Level 3", 0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4, plantHeight - plantSize * 2, plantSize * 6, plantHeight));

        experience = new GLText("EXPERIENCE", 0, 0);
    }

    private void createVideoTapes() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        for(int i = 0 ; i < texts.length ; i++) {

            float left = plantSize * 8 + VIDEO_TAPES_SIZE * i;
            float top = plantHeight / 2 - VIDEO_TAPES_SIZE / 2;
            float right = left + VIDEO_TAPES_SIZE;
            float bottom = top + VIDEO_TAPES_SIZE;

            Image image = new Image(new RectF(left, top, right, bottom));

            videoTapes.add(image);

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

    public void draw(float[] mvpMatrix) {

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        experience.draw(mvpMatrix);

        drawVideoTapes(mvpMatrix);

        drawPlants(mvpMatrix);

    }

    public void drawVideoTapes(float[] mvpMatrix) {

        int count = 0;
        for(Image image : videoTapes) {
            image.draw(mvpMatrix, 10);
            count++;
        }
    }

    public void drawPlants(float[] mvpMatrix) {

        int count = 0;
        for(Image image : plants) {
            image.draw(mvpMatrix, 2);
            count++;
        }
    }

    public void computeRect() {

        computePlants();
        computeLevelSign();
        computeExperience();
        computeVideoTapes();

    }

    private void computeLevelSign() {

        RectF srcRect = levelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        levelSign.setDstRect(left, top, right, bottom);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 1.5f,
                levelSign.getDstRect().top + srcRect.height() / 4);
    }

    private void computeExperience() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        experience.setLocation(signWood.getDstRect().centerX() - signText.getWidth() / 2,
                signWood.getDstRect().top + experience.getHeight() / 2);

    }


    private void computeVideoTapes() {

        float plantSize = Until.dp2px(context.getResources().getDisplayMetrics().density, 50);

        int count = 0;
        for(Image image : videoTapes) {

            float left = dstRect.left + image.getSrcRect().left;
            float top = image.getSrcRect().top;
            float right = left + VIDEO_TAPES_SIZE;
            float bottom = top + VIDEO_TAPES_SIZE;

            image.setDstRect(left, top, right, bottom);

            count++;
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
