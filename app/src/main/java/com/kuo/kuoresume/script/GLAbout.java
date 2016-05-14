package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLAbout extends ComputeRect{

    private static final int SEA_MIN_SIZE = 12;
    private static final int SEA_MAX_SIZE = 15;
    private static final int CLOUD_SIZE = 5;
    private static final int CLOUD_WIDTH = 140;
    private static final int CLOUD_HEIGHT = 95;

    private ArrayList<Image> plants = new ArrayList<>();
    private ArrayList<Image> clouds = new ArrayList<>();

    private Image levelSign, ticketStation, build85, signWood, buddha;

    private GLImageText glImageText1, glImageText2;

    private GLText signText, aboutText;

    private GLTrees glTrees;

    public GLAbout(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        //PLANT_RANGE_SIZE = (int) (viewComputeListener.getViewCompute().getContentRect().width() / viewComputeListener.getViewCompute().getPlantSize()) + 1;

        PLANT_RANGE_SIZE = 27;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        createClouds();
        createPlants();
        createImages();

        setDstRect(0, 0, width, height);
    }

    public void draw(float[] mvpMatrix) {

        glTrees.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        drawClouds(mvpMatrix);
        drawPlants(mvpMatrix);

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        aboutText.draw(mvpMatrix);

        ticketStation.draw(mvpMatrix, 5);

        build85.draw(mvpMatrix, 8);
        buddha.draw(mvpMatrix, 16);
        glImageText1.draw(mvpMatrix);
    }

    private void drawClouds(float[] m) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        for(Image image : clouds) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(m, 11);

        }

    }

    public void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        int count = 0;
        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                if (count >= SEA_MIN_SIZE && count <= SEA_MAX_SIZE)
                    image.draw(mvpMatrix, 9);
                else
                    image.draw(mvpMatrix, 2);
            }
            count++;
        }
    }

    private void createImages() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        float scaling = viewComputeListener.getScaling();

        levelSign = new Image(new RectF(0,
                plantHeight - ImageDefaultSize.SIGN_HEIGHT * scaling,
                ImageDefaultSize.SIGN_WIDTH * scaling,
                plantHeight));

        String signString = "Level 1";
        signText = new GLText(signString, (int) (ImageDefaultSize.SIGN_TEXT_SIZE * scaling),
                0, plantHeight - plantSize * 4);

        signWood = new Image(new RectF(plantSize * 4,
                plantHeight - ImageDefaultSize.SIGN_WOOD_HEIGHT * scaling,
                plantSize * 4 + ImageDefaultSize.SIGN_WOOD_WIDTH * scaling,
                plantHeight));

        aboutText = new GLText("about",(int) (ImageDefaultSize.SIGN_WOOD_TEXT_SIZE * scaling),
                0, 0);

        ticketStation = new Image(new RectF(plantSize * 9, plantHeight - plantSize * 2, plantSize * 11, plantHeight));

        build85 = new Image(new RectF(plantSize * 13,
                plantHeight - ImageDefaultSize.BUILD85_HEIGHT * scaling,
                plantSize * 13 + ImageDefaultSize.BUILD85_WIDTH * scaling, plantHeight));

        buddha = new Image(new RectF(plantSize * 16,
                plantHeight - ImageDefaultSize.BUDDHA_HEIGHT * scaling,
                plantSize * 16 + ImageDefaultSize.BUDDHA_WIDTH * scaling,
                plantHeight));

        glImageText1 = new GLImageText(context, "Live in Kaohsiung City", plantSize * 14, plantHeight / 6);

        glTrees = new GLTrees(6, scaling, width, height);
    }

    public void createClouds() {

        for(int i = 0 ; i < CLOUD_SIZE ; i++) {

            Random random = new Random();

            float left = random.nextInt(width - CLOUD_WIDTH);
            float top = random.nextInt(height / 2);
            float right = left + CLOUD_WIDTH;
            float bottom = top + CLOUD_HEIGHT;

            clouds.add(new Image(new RectF(left, top, right, bottom)));
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

    public void computeRect() {
        glTrees.computeTrees(dstRect);
        computeClouds();
        computePlants();
        computeAbout();
        computeLevelSign();
        computeTicketStation();
        computeBuild85();
        computeBuddha();
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

    private void computeAbout() {

        RectF srcRect = signWood.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top =  dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        signWood.setDstRect(left, top, right, bottom);

        aboutText.setLocation(signWood.getDstRect().centerX() - aboutText.getWidth() / 2,
                signWood.getDstRect().top + aboutText.getHeight() / 2);

    }

    private void computeTicketStation() {

        RectF srcRect = ticketStation.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        ticketStation.setDstRect(left, top, right, bottom);

    }

    private void computeBuild85() {

        RectF srcRect = build85.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        build85.setDstRect(left, top, right, bottom);

        RectF textSrcRect = glImageText1.getSrcRect();

        glImageText1.setLocation(build85.getDstRect().centerX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
    }

    private void computeBuddha() {

        RectF srcRect = buddha.getSrcRect();

        float left = dstRect.left + srcRect.left;
        float top = dstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        buddha.setDstRect(left, top, right, bottom);
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

    private void computeClouds() {

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
