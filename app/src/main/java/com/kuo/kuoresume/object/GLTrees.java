package com.kuo.kuoresume.object;

import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuo on 2016/5/11.
 */
public class GLTrees {

    private static final int TREE_WIDTH = 117 * 2;
    private static final int TREE_HEIGHT = 172 * 2;
    //private static final int[] TREE_POSITION = {13, 14, 15};

    private int TREE_SIZE = 5, width, height;

    private Random random = new Random();

    private ArrayList<Image> trees = new ArrayList<>();

    public GLTrees(int treeSize, int width, int height) {

        TREE_SIZE = treeSize;

        this.width = width;
        this.height = height;

        createTrees();
    }

    private void createTrees() {

        for(int i = 0 ; i < TREE_SIZE ; i++) {

            float left = random.nextInt(width - TREE_WIDTH);
            float top = height - TREE_HEIGHT - 150;
            float right = left + TREE_WIDTH;
            float bottom = top + TREE_HEIGHT;

            trees.add(new Image(new RectF(left, top, right, bottom)));
        }
    }

    public void draw(float[] m, RectF contentRect) {

        for(Image image : trees) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(m, 14);

        }
    }

    public void computeTrees(RectF dstRect) {

        for(Image image : trees) {

            RectF srcRect = image.getSrcRect();

            float left = dstRect.left + srcRect.left;
            float top = dstRect.top + srcRect.top;
            float right = left + srcRect.width();
            float bottom = top + srcRect.height();

            image.setDstRect(left, top, right, bottom);

        }
    }
}
