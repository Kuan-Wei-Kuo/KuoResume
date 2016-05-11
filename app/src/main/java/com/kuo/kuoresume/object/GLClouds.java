package com.kuo.kuoresume.object;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuo on 2016/5/11.
 */
public class GLClouds {

    private static final int CLOUD_WIDTH = 140 * 2;
    private static final int CLOUD_HEIGHT = 95 * 2;

    private int CLOUD_SIZE = 5, width, height;

    private ArrayList<Image> clouds = new ArrayList<>();

    public GLClouds(int cloudSize, int width, int height) {

        CLOUD_SIZE = cloudSize;

        this.width = width;
        this.height = height;

        createClouds();
    }

    private void createClouds() {

        for(int i = 0 ; i < CLOUD_SIZE ; i++) {

            Random random = new Random();

            float left = random.nextInt(width - CLOUD_WIDTH);
            float top = random.nextInt(height / 2);
            float right = left + CLOUD_WIDTH;
            float bottom = top + CLOUD_HEIGHT;

            clouds.add(new Image(new RectF(left, top, right, bottom)));
        }
    }

    public void draw(float[] m, RectF contentRect) {

        for(Image image : clouds) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(m, 11);

        }
    }

    public void computeClouds(RectF dstRect) {

        for(Image image : clouds) {

            RectF srcRect = image.getSrcRect();

            float left = dstRect.left + srcRect.left;
            float top = dstRect.top + srcRect.top;
            float right = left + srcRect.width();
            float bottom = top + srcRect.height();

            image.setDstRect(left, top, right, bottom);

        }
    }
}
