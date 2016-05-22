package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLClouds;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by User on 2016/5/8.
 */
public class GLExperience extends ComputeRect {

    private static final int PLANT_FLOOR_SIZE = 1;

    private static final float PRODUCT_NAME_UV_BOX_WIDTH = 0.25f;

    private float PRODUCT_ICON_SIZE = 200;

    private float PRODUCT_NAME_WIDTH = 150;
    private float PRODUCT_NAME_HEIGHT = 39;

    private ArrayList<GLSquare> squares = new ArrayList<>();

    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Image> productNames = new ArrayList<>();

    private ArrayList<GLImageText> glImageTexts = new ArrayList<>();

    private String[] texts = {"MyChart Lib", "FirstAid Sprite", "Basketball Board", "Ur Coco", "My Blog"};

    private float EXPERIENCE_ICON_SIZE = 384;

    private Image experienceIcon;

    public GLExperience(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 35;

        PRODUCT_ICON_SIZE = PRODUCT_ICON_SIZE * viewComputeListener.getScaling();

        EXPERIENCE_ICON_SIZE = EXPERIENCE_ICON_SIZE * viewComputeListener.getScaling();

        PRODUCT_NAME_WIDTH = PRODUCT_NAME_WIDTH * viewComputeListener.getScaling();
        PRODUCT_NAME_HEIGHT = PRODUCT_NAME_HEIGHT * viewComputeListener.getScaling();

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        experienceIcon = new Image(new RectF(0,
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

            float left = plantSize * 8 + PRODUCT_ICON_SIZE * i;
            float top = height / 2 - PRODUCT_ICON_SIZE / 2;
            float right = left + PRODUCT_ICON_SIZE;
            float bottom = top + PRODUCT_ICON_SIZE;

            images.add(new Image(new RectF(left, top, right, bottom)));

            Image productName = new Image(new RectF(left + ((right - left - PRODUCT_NAME_WIDTH) / 2), bottom,
                        left + ((right - left - PRODUCT_NAME_WIDTH) / 2) + PRODUCT_NAME_WIDTH, bottom + PRODUCT_NAME_HEIGHT));

            Log.d("w1", (right - left) + "");
            Log.d("w2", PRODUCT_NAME_WIDTH + "");

            productName.setUVS(new float[]{PRODUCT_NAME_UV_BOX_WIDTH * i, 0,
                    PRODUCT_NAME_UV_BOX_WIDTH * i, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 1,
                    PRODUCT_NAME_UV_BOX_WIDTH * i + PRODUCT_NAME_UV_BOX_WIDTH, 0});

            productNames.add(productName);


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

        int count = 0;
        for(Image image : images) {
            image.draw(mvpMatrix, 18 + count);
            count++;
        }


        for(Image image : productNames)
            image.draw(mvpMatrix, 11);


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

        for(Image image : images)
            image.computeDstRect(dstRect);

        for(Image image : productNames)
            image.computeDstRect(dstRect);
    }

}
