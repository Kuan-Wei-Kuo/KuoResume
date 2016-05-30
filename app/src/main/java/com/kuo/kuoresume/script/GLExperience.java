package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLImage;

import java.util.ArrayList;

/**
 * Created by User on 2016/5/8.
 */
public class GLExperience extends ComputeRect {

    private static final int PLANT_FLOOR_SIZE = 1;

    private static final float PRODUCT_384PX_UV_BOX_WIDTH = 0.2f;

    private static final float PRODUCT_NAME_UV_BOX_WIDTH = 0.25f;

    private static final float PRODUCT_INTERVIEW_UV_BOX_WIDTH = 0.25f;

    private float PRODUCT_ICON_SIZE = 160;

    private float WHITE_CARD_WIDTH = 550;
    private float WHITE_CARD_HEIGHT = 250;

    private float PRODUCT_INTERVIEW_WIDTH = 300;
    private float PRODUCT_INTERVIEW_HEIGHT = 90;

    private float PRODUCT_384px_SIZE = 200;

    private float EXPERIENCE_ICON_SIZE = 384;

    private float PRODUCT_NAME_WIDTH = 150;
    private float PRODUCT_NAME_HEIGHT = 39;

    private ArrayList<GLSquare> squares = new ArrayList<>();

    private ArrayList<GLImage> GLImages = new ArrayList<>();
    private ArrayList<GLImage> productNames = new ArrayList<>();
    private ArrayList<GLImage> productInterviews = new ArrayList<>();
    private ArrayList<GLImage> productCards = new ArrayList<>();

    private ArrayList<GLImageText> glImageTexts = new ArrayList<>();

    private String[] texts = {"MyChart Lib", "FirstAid Sprite", "Basketball Board", "Ur Coco"};

    private GLImage experienceIcon;

    public GLExperience(Context context, ViewComputeListener viewComputeListener) {
        super(context, viewComputeListener);

        PLANT_RANGE_SIZE = 70;

        PRODUCT_ICON_SIZE = PRODUCT_ICON_SIZE * viewComputeListener.getScaling();

        EXPERIENCE_ICON_SIZE = EXPERIENCE_ICON_SIZE * viewComputeListener.getScaling();

        PRODUCT_NAME_WIDTH = PRODUCT_NAME_WIDTH * viewComputeListener.getScaling();
        PRODUCT_NAME_HEIGHT = PRODUCT_NAME_HEIGHT * viewComputeListener.getScaling();

        PRODUCT_INTERVIEW_WIDTH = PRODUCT_INTERVIEW_WIDTH * viewComputeListener.getScaling();
        PRODUCT_INTERVIEW_HEIGHT = PRODUCT_INTERVIEW_HEIGHT * viewComputeListener.getScaling();

        WHITE_CARD_WIDTH = WHITE_CARD_WIDTH * viewComputeListener.getScaling();
        WHITE_CARD_HEIGHT = WHITE_CARD_HEIGHT * viewComputeListener.getScaling();

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        experienceIcon = new GLImage(new RectF(0,
                height - viewComputeListener.getViewCompute().getPlantSize() - EXPERIENCE_ICON_SIZE,
                EXPERIENCE_ICON_SIZE, height - viewComputeListener.getViewCompute().getPlantSize()));

        createSquares();
        createProductIconAndName();
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
    }

    private void createProductIconAndName() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int i = 0 ; i < texts.length ; i++) {

            float left = plantSize * 8 + (WHITE_CARD_WIDTH + WHITE_CARD_WIDTH * 0.2f) * i;
            float top = height / 2 - WHITE_CARD_HEIGHT / 2;
            float right = left + WHITE_CARD_WIDTH;
            float bottom = top + WHITE_CARD_HEIGHT;

            GLImage productCard = new GLImage(new RectF(left, top, right, bottom));

            productCard.setUVS(new float[]{PRODUCT_384PX_UV_BOX_WIDTH * texts.length, 0,
                    PRODUCT_384PX_UV_BOX_WIDTH * texts.length, 1,
                    PRODUCT_384PX_UV_BOX_WIDTH * texts.length + PRODUCT_384PX_UV_BOX_WIDTH, 1,
                    PRODUCT_384PX_UV_BOX_WIDTH * texts.length + PRODUCT_384PX_UV_BOX_WIDTH, 0});

            productCards.add(productCard);


            left += PRODUCT_ICON_SIZE * 0.2f;
            top = height / 2 - PRODUCT_ICON_SIZE / 2;
            right = left + PRODUCT_ICON_SIZE;
            bottom = top + PRODUCT_ICON_SIZE;

            GLImage GLImage = new GLImage(new RectF(left, top, right, bottom));

            GLImage.setUVS(new float[]{PRODUCT_384PX_UV_BOX_WIDTH * i, 0,
                    PRODUCT_384PX_UV_BOX_WIDTH * i, 1,
                    PRODUCT_384PX_UV_BOX_WIDTH * i + PRODUCT_384PX_UV_BOX_WIDTH, 1,
                    PRODUCT_384PX_UV_BOX_WIDTH * i + PRODUCT_384PX_UV_BOX_WIDTH, 0});

            GLImages.add(GLImage);

            GLImage productName = new GLImage(new RectF(right + PRODUCT_NAME_WIDTH * 0.2f, top,
                    right + PRODUCT_NAME_WIDTH * 0.2f + PRODUCT_NAME_WIDTH, top + PRODUCT_NAME_HEIGHT));

            productName.setUVS(new float[]{PRODUCT_NAME_UV_BOX_WIDTH * i, 0,
                    PRODUCT_NAME_UV_BOX_WIDTH * i, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 0});

            productNames.add(productName);

            GLImage productInterview = new GLImage(new RectF(right + PRODUCT_NAME_WIDTH * 0.2f, top + PRODUCT_NAME_HEIGHT * 1.2f,
                    right + PRODUCT_NAME_WIDTH * 0.2f + PRODUCT_INTERVIEW_WIDTH, top + PRODUCT_NAME_HEIGHT * 1.2f + PRODUCT_INTERVIEW_HEIGHT));

            productInterview.setUVS(new float[]{PRODUCT_NAME_UV_BOX_WIDTH * i, 0,
                    PRODUCT_NAME_UV_BOX_WIDTH * i, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 0});

            productInterviews.add(productInterview);
        }
    }


    public void draw(float[] mvpMatrix) {

        experienceIcon.draw(mvpMatrix, 3);
        drawSquares(mvpMatrix);
        drawImages(mvpMatrix);
        drawGLImageTexts(mvpMatrix);

    }

    private void drawGLImageTexts(float[] mvpMatrix) {

        for(GLImageText glImageText : glImageTexts) {
            glImageText.draw(mvpMatrix);
        }
    }

    private void drawImages(float[] mvpMatrix) {

        for(GLImage GLImage : productCards)
            GLImage.draw(mvpMatrix, 18);

        for(GLImage GLImage : GLImages)
            GLImage.draw(mvpMatrix, 18);

        for(GLImage GLImage : productNames)
            GLImage.draw(mvpMatrix, 11);

        for(GLImage GLImage : productInterviews)
            GLImage.draw(mvpMatrix, 19);
    }

    private void computeSquares() {

        for (GLSquare glSquare : squares)
            glSquare.computeDstRect(dstRect);

    }

    private void drawSquares(float[] mvpMatrix) {

        for(GLSquare glSquare : squares)
            glSquare.draw(mvpMatrix);

    }

    public void computeRect() {
        experienceIcon.computeDstRect(dstRect);
        computeSquares();
        computeImages();
        computeGLImageTexts();

    }

    private void computeGLImageTexts() {

        for(GLImageText glImageText : glImageTexts) {

            float left = dstRect.left + glImageText.getX();
            float top = dstRect.top + glImageText.getY();

            glImageText.setLocation(left, top);

        }
    }

    private void computeImages() {

        for(GLImage GLImage : GLImages)
            GLImage.computeDstRect(dstRect);

        for(GLImage GLImage : productNames)
            GLImage.computeDstRect(dstRect);

        for(GLImage GLImage : productCards)
            GLImage.computeDstRect(dstRect);

        for(GLImage GLImage : productInterviews)
            GLImage.computeDstRect(dstRect);
    }

}
