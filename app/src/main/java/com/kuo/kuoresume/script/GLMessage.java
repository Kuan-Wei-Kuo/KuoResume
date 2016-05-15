package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/9.
 */
public class GLMessage extends ComputeRect {

    private static final int MIN_SEA = 1;
    private static final int MAX_SEA = 9;

    private Image shareImage, contactImage, githubImage, goldBoxImage;

    private ActivityListener activityListener;

    private ArrayList<Image> plants = new ArrayList<>();

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener, ActivityListener activityListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 20;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createPlants();
        createImage();
        computeRect();

        this.activityListener = activityListener;
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

        goldBoxImage = new Image(new RectF(width - plantSize * 2 - imageSize, height - plantSize - imageSize,
                width - plantSize * 2, height - plantSize));

    }

    public void draw(float[] m) {

        contactImage.draw(m, 23);
        githubImage.draw(m, 25);
        shareImage.draw(m, 24);
        goldBoxImage.draw(m, 26);
        drawPlants(m);

    }

    private void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                if(count >= MIN_SEA && count <= MAX_SEA)
                    image.draw(mvpMatrix, 9);
                else
                    image.draw(mvpMatrix, 2);

            }
            count++;
        }
    }

    public void computeRect() {
        computePlants();
        computeContactImage();
        computeShareImage();
        computeGithubImage();
        computeGoldBoxImage();
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

    private void computeContactImage() {

        RectF srcRect = contactImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        contactImage.setDstRect(left, top, right, bottom);

    }

    private void computeGithubImage() {

        RectF srcRect = githubImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        githubImage.setDstRect(left, top, right, bottom);

    }

    private void computeShareImage() {

        RectF srcRect = shareImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        shareImage.setDstRect(left, top, right, bottom);

    }

    private void computeGoldBoxImage() {

        RectF srcRect = goldBoxImage.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        goldBoxImage.setDstRect(left, top, right, bottom);

    }

    private void computePlants() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

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
