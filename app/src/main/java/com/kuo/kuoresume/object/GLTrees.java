package com.kuo.kuoresume.object;

import android.graphics.RectF;

import com.kuo.kuoresume.compute.ImageDefaultSize;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuo on 2016/5/11.
 */
public class GLTrees {

    private int TREE_WIDTH = 117;
    private int TREE_HEIGHT = 172;
    //private static final int[] TREE_POSITION = {13, 14, 15};

    private int TREE_SIZE = 5;

    private float scaling, width, height;

    private Random random = new Random();

    private ArrayList<GLImage> trees = new ArrayList<>();

    public GLTrees(int treeSize, float scaling, float width, float height) {

        TREE_WIDTH = (int) (TREE_WIDTH * scaling);
        TREE_HEIGHT = (int) (TREE_HEIGHT * scaling);

        TREE_SIZE = treeSize;

        this.width = width;
        this.height = height;
        this.scaling = scaling;

        createTrees();
    }

    private void createTrees() {

        for(int i = 0 ; i < TREE_SIZE ; i++) {

            float left = random.nextInt((int) (width - TREE_WIDTH));
            float top = height - TREE_HEIGHT - ImageDefaultSize.PLANT_SIZE * scaling;
            float right = left + TREE_WIDTH;
            float bottom = top + TREE_HEIGHT;

            trees.add(new GLImage(new RectF(left, top, right, bottom)));
        }
    }

    public void draw(float[] m, RectF contentRect) {

        for(GLImage GLImage : trees) {

            RectF rectF = GLImage.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                GLImage.draw(m, 14);

        }
    }

    public void computeTrees(RectF dstRect) {

        for(GLImage GLImage : trees) {

            RectF srcRect = GLImage.getSrcRect();

            float left = dstRect.left + srcRect.left;
            float top = dstRect.top + srcRect.top;
            float right = left + srcRect.width();
            float bottom = top + srcRect.height();

            GLImage.setDstRect(left, top, right, bottom);

        }
    }
}
