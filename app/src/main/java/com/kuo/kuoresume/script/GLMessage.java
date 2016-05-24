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

    public static final int PLANT_FLOOR_SIZE = 10;
    public static final int MIN_NULL = 5;
    public static final int MAX_NULL = 30;

    private GLImage shareGLImage, contactGLImage, githubGLImage, goldBoxGLImage, flickerLight, bgBuild;

    private GLAirPlane glAirPlane;

    private GLSquare jumpSquare;

    private SpriteController flickerLightSprite;

    private ActivityListener activityListener;

    private ArrayList<GLSquare> squares = new ArrayList<>();

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener, ActivityListener activityListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 40;

        width = PLANT_RANGE_SIZE * viewComputeListener.getViewCompute().getPlantSize();

        height = (PLANT_FLOOR_SIZE - 1) * viewComputeListener.getViewCompute().getPlantSize() + viewComputeListener.getViewCompute().getContentRect().height();

        FLICKER_LIGHT_WIDTH = FLICKER_LIGHT_WIDTH * viewComputeListener.getScaling();
        FLICKER_LIGHT_HEIGHT = FLICKER_LIGHT_HEIGHT * viewComputeListener.getScaling();

        PAPER_AIR_PLANT_WIDTH = PAPER_AIR_PLANT_WIDTH * viewComputeListener.getScaling();
        PAPER_AIR_PLANT_HEIGHT = PAPER_AIR_PLANT_HEIGHT * viewComputeListener.getScaling();

        bgBuild = new GLImage(new RectF(0, 0, width, height));

        createSquares();
        createImage();
        computeRect();

        this.activityListener = activityListener;
    }

    private void createSquares() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float[] color = new float[] {0, 0, 0, 1f};

        for(int j = 0 ; j < PLANT_FLOOR_SIZE ; j++) {

            float left = MAX_NULL * plantSize;
            float top = height - plantSize - plantSize * j;
            float right = left + width;
            float bottom = top + plantSize;

            squares.add(new GLSquare(new RectF(left, top, right, bottom), color));
        }

        jumpSquare = new GLSquare(new RectF(MIN_NULL * plantSize, height,
                width, height), color);

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        glAirPlane = new GLAirPlane(context, viewComputeListener, objectListener);
        glAirPlane.setSrcRect(MIN_NULL * plantSize - PAPER_AIR_PLANT_WIDTH,
                contentRect.bottom - plantSize - PAPER_AIR_PLANT_HEIGHT ,
                MIN_NULL * plantSize, contentRect.bottom - plantSize );

        glAirPlane.setDstRect(dstRect.left, 0, 0, 0);
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
                shareGLImage = new GLImage(new RectF(left, top, right, bottom));
            else if (i == 1)
                githubGLImage = new GLImage(new RectF(left, top, right, bottom));
            else if (i == 2)
                contactGLImage = new GLImage(new RectF(left, top, right, bottom));
        }

        goldBoxGLImage = new GLImage(new RectF(width - plantSize * 2 - imageSize,
                height - plantSize - imageSize,
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
        bgBuild.draw(m, 20);
        glAirPlane.draw(m);
        flickerLightSprite.start();
        contactGLImage.draw(m, 23);
        githubGLImage.draw(m, 25);
        shareGLImage.draw(m, 24);
        flickerLight.draw(m, 29);
        goldBoxGLImage.draw(m, 30);
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

        bgBuild.setDstRect(dstRect.left, 0, dstRect.right, height);
        contactGLImage.computeDstRect(dstRect);
        githubGLImage.computeDstRect(dstRect);
        shareGLImage.computeDstRect(dstRect);
        goldBoxGLImage.computeDstRect(dstRect);
        flickerLight.computeDstRect(dstRect);

        RectF characterRect = viewComputeListener.getGLCharacter().getDstRect();

        if(characterRect.right > dstRect.left + 0 * viewComputeListener.getViewCompute().getPlantSize()
                && characterRect.right < dstRect.left + MAX_NULL * viewComputeListener.getViewCompute().getPlantSize()) {

            glAirPlane.start();
            glAirPlane.setDstRect(characterRect.centerX() - PAPER_AIR_PLANT_WIDTH / 2, 0, 0, 0);

            float height =  characterRect.height();

            viewComputeListener.getGLCharacter().setDstRect(
                    characterRect.left,
                    glAirPlane.getSrcRect().top - height,
                    characterRect.right,
                    glAirPlane.getSrcRect().top);

            viewComputeListener.getGLCharacter().setCharacterState(GLCharacter.CHARACTER_IDLE);

        } else if(characterRect.left < dstRect.left
                && characterRect.right < dstRect.left) {

            viewComputeListener.getGLCharacter().setDstRect(
                    characterRect.left,
                    viewComputeListener.getGLCharacter().getSrcRect().top,
                    characterRect.right,
                    viewComputeListener.getGLCharacter().getSrcRect().bottom);

            glAirPlane.end();
            glAirPlane.setDstRect(dstRect.left, 0, 0, 0);
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
