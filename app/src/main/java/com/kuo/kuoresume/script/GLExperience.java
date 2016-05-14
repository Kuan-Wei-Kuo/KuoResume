package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLClouds;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by User on 2016/5/8.
 */
public class GLExperience extends ComputeRect {

    private static final int VIDEO_TAPES_SIZE = 550;

    private ArrayList<Image> plants = new ArrayList<>();

    private Image levelSign, signWood;

    private GLText signText, experience;

    private GLClouds glClouds;

    private GLTrees glTrees;

    private ArrayList<Image> videoTapes = new ArrayList<>();
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<GLImageText> glImageTexts = new ArrayList<>();

    private String[] texts = {"MyChartLib", "FirstAidSprite", "Basketball Board", "UrCoco", "MyBlog"};

    public GLExperience(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 30;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createImage();
        createVideoTapes();
        computeRect();
    }

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        float scaling = viewComputeListener.getScaling();

        levelSign = new Image(new RectF(0,
                plantHeight - ImageDefaultSize.SIGN_HEIGHT * scaling,
                ImageDefaultSize.SIGN_WIDTH * scaling,
                plantHeight));

        String signString = "Level 3";
        signText = new GLText(signString, (int) (ImageDefaultSize.SIGN_TEXT_SIZE * scaling),
                0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4,
                plantHeight - ImageDefaultSize.SIGN_WOOD_HEIGHT * scaling,
                plantSize * 4 + ImageDefaultSize.SIGN_WOOD_WIDTH * scaling,
                plantHeight));

        experience = new GLText("experience",(int) (ImageDefaultSize.SIGN_WOOD_TEXT_SIZE * scaling),
                0, 0);

        glClouds = new GLClouds(7, width, height);

        glTrees = new GLTrees(6, viewComputeListener.getScaling(), width, height);
    }

    private void createVideoTapes() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        for(int i = 0 ; i < texts.length ; i++) {

            float left = plantSize * 8 + VIDEO_TAPES_SIZE * i;
            float top = plantHeight / 2 - VIDEO_TAPES_SIZE / 2;
            float right = left + VIDEO_TAPES_SIZE;
            float bottom = top + VIDEO_TAPES_SIZE;

            Image taps = new Image(new RectF(left, top, right, bottom));

            Image image = new Image(new RectF(taps.getSrcRect().centerX() - VIDEO_TAPES_SIZE * 0.8f / 2,
                    taps.getSrcRect().centerY() - VIDEO_TAPES_SIZE * 0.8f / 2,
                    taps.getSrcRect().centerX() + VIDEO_TAPES_SIZE * 0.8f / 2,
                    taps.getSrcRect().centerY() + VIDEO_TAPES_SIZE * 0.8f / 2));

            GLImageText glImageText = new GLImageText(context, texts[i], 0, 0);

            glImageText.setPosition(taps.getSrcRect().centerX() - glImageText.getSrcRect().width() / 2,
                    bottom + glImageText.getSrcRect().height());

            glImageTexts.add(glImageText);

            images.add(image);
            videoTapes.add(taps);

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

        glClouds.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        glTrees.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        experience.draw(mvpMatrix);

        drawVideoTapes(mvpMatrix);
        drawImages(mvpMatrix);
        drawGLImageTexts(mvpMatrix);

        drawPlants(mvpMatrix);
    }

    private void drawGLImageTexts(float[] mvpMatrix) {

        int count = 0;
        for(GLImageText glImageText : glImageTexts) {
            glImageText.draw(mvpMatrix);
            count++;
        }
    }

    private void drawImages(float[] mvpMatrix) {

        int count = 0;
        for(Image image : images) {
            image.draw(mvpMatrix, 3);
            count++;
        }
    }

    private void drawVideoTapes(float[] mvpMatrix) {

        int count = 0;
        for(Image image : videoTapes) {
            image.draw(mvpMatrix, 10);
            count++;
        }
    }

    private void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(mvpMatrix, 2);

            count++;
        }
    }

    public void computeRect() {
        glClouds.computeClouds(dstRect);
        glTrees.computeTrees(dstRect);
        computePlants();
        computeLevelSign();
        computeExperience();
        computeVideoTapes();
        computeImages();
        computeGLImageTexts();

    }

    private void computeLevelSign() {

        RectF srcRect = levelSign.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        levelSign.setDstRect(left, top, right, bottom);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + signText.getHeight());
    }

    private void computeExperience() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        experience.setLocation(signWood.getDstRect().centerX() - experience.getWidth() / 2,
                signWood.getDstRect().top + experience.getHeight() / 2);

    }


    private void computeVideoTapes() {

        int count = 0;
        for(Image image : videoTapes) {

            float left = dstRect.left + image.getSrcRect().left;
            float top = dstRect.top + image.getSrcRect().top;
            float right = left + VIDEO_TAPES_SIZE;
            float bottom = top + VIDEO_TAPES_SIZE;

            image.setDstRect(left, top, right, bottom);

            count++;
        }
    }

    private void computeGLImageTexts() {

        int count = 0;
        for(GLImageText glImageText : glImageTexts) {

            float left = dstRect.left + glImageText.getX();
            float top = dstRect.top + glImageText.getY();
            float right = left + VIDEO_TAPES_SIZE * 0.8f;
            float bottom = top + VIDEO_TAPES_SIZE * 0.8f;

            glImageText.setLocation(left, top);

            count++;
        }
    }

    private void computeImages() {

        int count = 0;
        for(Image image : images) {

            float left = dstRect.left + image.getSrcRect().left;
            float top = dstRect.top + image.getSrcRect().top;
            float right = left + VIDEO_TAPES_SIZE * 0.8f;
            float bottom = top + VIDEO_TAPES_SIZE * 0.8f;

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
