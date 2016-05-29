package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;
import com.kuo.kuoresume.object.GLSquare;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/9.
 */
public class GLMessage extends ComputeRect {

    private float FLICKER_LIGHT_WIDTH = 156;
    private float FLICKER_LIGHT_HEIGHT = 156;


    private float FLICKER_LIGHT_UV_BOX_WIDTH = 0.333f;

    public float PAPER_AIR_PLANT_WIDTH = 384 * 0.5f;
    public float PAPER_AIR_PLANT_HEIGHT = 136 * 0.5f;

    public static final int PLANT_FLOOR_SIZE = 1;
    public static final int MIN_NULL = 5;
    public static final int MAX_NULL = 10;

    private GLImage shareGLImage, contactGLImage, githubGLImage, goldBoxGLImage, flickerLight, bgBuild;

    private GLSquare jumpSquare, ground;

    private SpriteController flickerLightSprite;

    private ActivityListener activityListener;

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener, ActivityListener activityListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 20;

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (PLANT_FLOOR_SIZE - 1) * viewComputeListener.getViewCompute().getPlantSize() + viewComputeListener.getViewCompute().getContentRect().height();

        FLICKER_LIGHT_WIDTH = FLICKER_LIGHT_WIDTH * viewComputeListener.getScaling();
        FLICKER_LIGHT_HEIGHT = FLICKER_LIGHT_HEIGHT * viewComputeListener.getScaling();

        PAPER_AIR_PLANT_WIDTH = PAPER_AIR_PLANT_WIDTH * viewComputeListener.getScaling();
        PAPER_AIR_PLANT_HEIGHT = PAPER_AIR_PLANT_HEIGHT * viewComputeListener.getScaling();

        bgBuild = new GLImage(new RectF(0, 0, width, height));

        createSquares();
        createImage();
        //computeRect();

        this.activityListener = activityListener;
    }

    private void createSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float[] color = new float[] {0, 0, 0, 1f};

        ground = new GLSquare(new RectF(0, height - plantSize, width, height), color);

        jumpSquare = new GLSquare(new RectF(0, height - plantSize * 3,
                MAX_NULL * plantSize, height - plantSize), color);

        jumpSquare.setColliderListener(viewComputeListener.getGLCharacter().getColliderListener());

    }

    private static final float IMAGE_SIZE = 100;

    private float GOLD_BOX_WIDTH = 250;
    private float GOLD_BOX_HEIGHT = 250;

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float imageSize = IMAGE_SIZE * viewComputeListener.getScaling();

        GOLD_BOX_WIDTH = GOLD_BOX_WIDTH * viewComputeListener.getScaling();
        GOLD_BOX_HEIGHT = GOLD_BOX_HEIGHT * viewComputeListener.getScaling();

        float left = 0, right = 0, top = 0, bottom = imageSize;

        for(int i = 0 ; i < 3 ; i++) {

            left = i > 0 ? left - imageSize : width - imageSize;
            right = left + imageSize;

            if (i == 0)
                shareGLImage = new GLImage(new RectF(left, top, right, bottom));
            else if (i == 1)
                githubGLImage = new GLImage(new RectF(left, top, right, bottom));
            else if (i == 2)
                contactGLImage = new GLImage(new RectF(left, top, right, bottom));
        }

        goldBoxGLImage = new GLImage(new RectF(width - plantSize * 2 - GOLD_BOX_WIDTH,
                height - plantSize - GOLD_BOX_HEIGHT,
                width - plantSize * 2,
                height - plantSize));

        flickerLight = new GLImage(new RectF(goldBoxGLImage.getSrcRect().centerX() - FLICKER_LIGHT_WIDTH / 2,
                goldBoxGLImage.getSrcRect().top - FLICKER_LIGHT_HEIGHT,
                goldBoxGLImage.getSrcRect().centerX() + FLICKER_LIGHT_WIDTH / 2,
                goldBoxGLImage.getSrcRect().top));

        flickerLightSprite = new SpriteController(350, 0, 0, 3);
        flickerLightSprite.setOnUpdateListener(flickerLightSpriteListener);
    }

    private SpriteController.OnUpdateListener flickerLightSpriteListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            float left = currentHorizontalFrame * FLICKER_LIGHT_UV_BOX_WIDTH;
            float right = left + FLICKER_LIGHT_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            flickerLight.setUVS(new float[] {
                    left, top,
                    left, bottom,
                    right, bottom,
                    right, top
            });
        }

        @Override
        public void onEnd() {

        }
    };

    public void draw(float[] m) {

        ground.draw(m);
        jumpSquare.draw(m);
        //flickerLightSprite.start();
        contactGLImage.draw(m, 23);
        githubGLImage.draw(m, 25);
        shareGLImage.draw(m, 24);
        //flickerLight.draw(m, 29);
        goldBoxGLImage.draw(m, 30);

    }

    public void computeRect() {

        jumpSquare.startCollider(viewComputeListener.getGLCharacter().getDstRect());
        jumpSquare.computeDstRect(dstRect);

        ground.computeDstRect(dstRect);

        bgBuild.setDstRect(dstRect.left, 0, dstRect.right, height);
        contactGLImage.computeDstRect(dstRect);
        githubGLImage.computeDstRect(dstRect);
        shareGLImage.computeDstRect(dstRect);
        goldBoxGLImage.computeDstRect(dstRect);
        flickerLight.computeDstRect(dstRect);

    }

    boolean isTactFocus = false;

    public boolean onTouchContact(MotionEvent e) {

        boolean isClick = false;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(contactGLImage.getDstRect().contains(e.getX(), e.getY()))
                    isTactFocus = true;
                break;
            case MotionEvent.ACTION_UP:
                if(isTactFocus && contactGLImage.getDstRect().contains(e.getX(), e.getY())) {
                    isClick = true;
                    activityListener.showDialogContact();
                }

                isTactFocus = false;

                break;

        }
        return isClick;
    }

    boolean isShareFocus = false;

    public boolean onTouchShare(MotionEvent e) {

        boolean isClick = false;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(shareGLImage.getDstRect().contains(e.getX(), e.getY()))
                    isShareFocus = true;
                break;
            case MotionEvent.ACTION_UP:

                if(isShareFocus  && shareGLImage.getDstRect().contains(e.getX(), e.getY())) {
                    isClick = true;
                    activityListener.showDialogShare();
                }

                isShareFocus = false;

                break;

        }
        return isClick;
    }

    boolean isGithubFocus = false;

    public boolean onTouchGithub(MotionEvent e) {

        boolean isClick = false;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(githubGLImage.getDstRect().contains(e.getX(), e.getY()))
                    isGithubFocus = true;
                break;
            case MotionEvent.ACTION_UP:

                if(isGithubFocus  && githubGLImage.getDstRect().contains(e.getX(), e.getY())) {
                    isClick = true;
                    activityListener.showGitHubBrowser();
                }

                isShareFocus = false;

                break;

        }
        return isClick;
    }

    public boolean isTactFocus() {
        return isTactFocus;
    }

    public boolean isShareFocus() {
        return isShareFocus;
    }

    public boolean isGithubFocus() {
        return isGithubFocus;
    }
}
