package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/9.
 */
public class GLMessage extends ComputeRect {

    private float FLICKER_LIGHT_WIDTH = 156;
    private float FLICKER_LIGHT_HEIGHT = 156;


    private float FLICKER_LIGHT_UV_BOX_WIDTH = 0.333f;

    public float PAPER_AIR_PLANT_WIDTH = 384;
    public float PAPER_AIR_PLANT_HEIGHT = 136;

    public static final int PLANT_FLOOR_SIZE = 1;
    public static final int MIN_NULL = 5;
    public static final int MAX_NULL = 12;

    private Image shareImage, contactImage, githubImage, goldBoxImage, flickerLight, bgBuild, paperAirPlant;

    private GLSquare jumpSquare;

    private SpriteController flickerLightSprite;

    private ActivityListener activityListener;

    private ArrayList<GLSquare> squares = new ArrayList<>();

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener, ActivityListener activityListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 20;

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = viewComputeListener.getViewCompute().getContentRect().height();

        FLICKER_LIGHT_WIDTH = FLICKER_LIGHT_WIDTH * viewComputeListener.getScaling();
        FLICKER_LIGHT_HEIGHT = FLICKER_LIGHT_HEIGHT * viewComputeListener.getScaling();

        PAPER_AIR_PLANT_WIDTH = PAPER_AIR_PLANT_WIDTH * viewComputeListener.getScaling();
        PAPER_AIR_PLANT_HEIGHT = PAPER_AIR_PLANT_HEIGHT * viewComputeListener.getScaling();

        bgBuild = new Image(new RectF(0, 0, width, height));

        createSquares();
        createImage();
        computeRect();

        this.activityListener = activityListener;
    }

    private void createSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float[] color = new float[] {0, 0, 0, 1f};

        for(int j = 0 ; j < 2 ; j++) {

            float left = j == 0 ? 0 : MAX_NULL * plantSize;
            float top = j == 0 ? height - plantSize : height - plantSize;
            float right = j == 0 ? left + MIN_NULL * plantSize : width;
            float bottom = j == 0 ? top + plantSize : height;

            squares.add(new GLSquare(new RectF(left, top, right, bottom), color));
        }

        jumpSquare = new GLSquare(new RectF(MIN_NULL * plantSize, height,
                width, height), color);

        paperAirPlant = new Image(new RectF(MIN_NULL * plantSize - PAPER_AIR_PLANT_WIDTH,
                height - plantSize - PAPER_AIR_PLANT_HEIGHT,
                MIN_NULL * plantSize, height - plantSize));

        //jumpSquare.setColliderListener(viewComputeListener.getGLCharacter().getJumpMessage());
    }

    private static final float IMAGE_SIZE = 110;

    private void createImage() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float imageSize = IMAGE_SIZE * viewComputeListener.getScaling();

        float left = 0, right = 0, top = 0, bottom = imageSize;

        for(int i = 0 ; i < 3 ; i++) {

            left = i > 0 ? left - imageSize : width - imageSize;
            right = left + imageSize;

            if (i == 0)
                shareImage = new Image(new RectF(left, top, right, bottom));
            else if (i == 1)
                githubImage = new Image(new RectF(left, top, right, bottom));
            else if (i == 2)
                contactImage = new Image(new RectF(left, top, right, bottom));
        }

        goldBoxImage = new Image(new RectF(width - plantSize * 2 - imageSize,
                height - plantSize - imageSize,
                width - plantSize * 2,
                height - plantSize));

        flickerLight = new Image(new RectF(goldBoxImage.getSrcRect().centerX() - FLICKER_LIGHT_WIDTH / 2,
                goldBoxImage.getSrcRect().top - FLICKER_LIGHT_HEIGHT,
                goldBoxImage.getSrcRect().centerX() + FLICKER_LIGHT_WIDTH / 2,
                goldBoxImage.getSrcRect().top));

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
        bgBuild.draw(m, 20);
        paperAirPlant.draw(m, 21);
        flickerLightSprite.start();
        contactImage.draw(m, 23);
        githubImage.draw(m, 25);
        shareImage.draw(m, 24);
        flickerLight.draw(m, 29);
        goldBoxImage.draw(m, 30);
        drawSquares(m);

    }

    private void drawSquares(float[] mvpMatrix) {

        for(GLSquare glSquare : squares)
            glSquare.draw(mvpMatrix);
    }

    public void computeRect() {
        computeSquares();

        jumpSquare.startCollider(viewComputeListener.getGLCharacter().getDstRect());
        jumpSquare.computeDstRect(dstRect);

        paperAirPlant.computeDstRect(dstRect);
        bgBuild.setDstRect(dstRect.left, 0, dstRect.right, height);
        contactImage.computeDstRect(dstRect);
        githubImage.computeDstRect(dstRect);
        shareImage.computeDstRect(dstRect);
        goldBoxImage.computeDstRect(dstRect);
        flickerLight.computeDstRect(dstRect);

        RectF characterRect = viewComputeListener.getGLCharacter().getDstRect();

        if(viewComputeListener.getGLCharacter().getDirection() == -1
                && characterRect.left < dstRect.left + MAX_NULL * viewComputeListener.getViewCompute().getPlantSize()
                && characterRect.right > dstRect.left + MAX_NULL * viewComputeListener.getViewCompute().getPlantSize()) {

            jumpSquare.updateListener(squares.get(0).getDstRect());

        }
    }

    private void computeSquares() {

        for (GLSquare glSquare : squares)
            glSquare.computeDstRect(dstRect);

    }

    boolean isTactFocus = false;

    public boolean onTouchContact(MotionEvent e) {

        boolean isClick = false;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(contactImage.getDstRect().contains(e.getX(), e.getY()))
                    isTactFocus = true;
                break;
            case MotionEvent.ACTION_UP:
                if(isTactFocus && contactImage.getDstRect().contains(e.getX(), e.getY())) {
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
                if(shareImage.getDstRect().contains(e.getX(), e.getY()))
                    isShareFocus = true;
                break;
            case MotionEvent.ACTION_UP:

                if(isShareFocus  && shareImage.getDstRect().contains(e.getX(), e.getY())) {
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
                if(githubImage.getDstRect().contains(e.getX(), e.getY()))
                    isGithubFocus = true;
                break;
            case MotionEvent.ACTION_UP:

                if(isGithubFocus  && githubImage.getDstRect().contains(e.getX(), e.getY())) {
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
