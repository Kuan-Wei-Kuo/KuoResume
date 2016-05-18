package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLText;
import com.kuo.kuoresume.object.GLTrees;
import com.kuo.kuoresume.object.Image;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLAbout extends ComputeRect{

    private static final float OWN_MUSIC_UV_BOX_WIDTH = 0.5f;

    private static final int SEA_MIN_SIZE = 12;
    private static final int SEA_MAX_SIZE = 15;
    private static final int CLOUD_SIZE = 5;
    private static final int CLOUD_WIDTH = 140;
    private static final int CLOUD_HEIGHT = 95;

    private float CHARACTER_MUSIC_WIDTH = 107;
    private float CHARACTER_MUSIC_HEIGHT = 142;

    private ArrayList<Image> plants = new ArrayList<>();
    private ArrayList<Image> clouds = new ArrayList<>();
    private ArrayList<GLSquare> squares = new ArrayList<>();

    private SpriteController ownMusicSpriteController;

    private Image levelSign, ticketStation, build85, signWood, buddha, characterMusic;

    private GLImageText glImageText1, glImageText2;

    private GLText signText, aboutText;

    private GLTrees glTrees;

    public GLAbout(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        //PLANT_RANGE_SIZE = (int) (viewComputeListener.getViewCompute().getContentRect().width() / viewComputeListener.getViewCompute().getPlantSize()) + 1;

        PLANT_RANGE_SIZE = 27;

        CHARACTER_MUSIC_WIDTH = CHARACTER_MUSIC_WIDTH * viewComputeListener.getScaling();
        CHARACTER_MUSIC_HEIGHT = CHARACTER_MUSIC_HEIGHT * viewComputeListener.getScaling();

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        ownMusicSpriteController = new SpriteController(300, 0, 0, 2);
        ownMusicSpriteController.setOnUpdateListener(onUpdateListener);

        createClouds();
        createPlants();
        createSquare();
        createImages();

        setDstRect(0, 0, width, height);
    }

    private SpriteController.OnUpdateListener onUpdateListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            float left = currentHorizontalFrame * OWN_MUSIC_UV_BOX_WIDTH;
            float right = left + OWN_MUSIC_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            characterMusic.setUVS(new float[] {
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

    public void draw(float[] mvpMatrix) {

        ownMusicSpriteController.start();

        glTrees.draw(mvpMatrix, viewComputeListener.getViewCompute().getContentRect());
        drawClouds(mvpMatrix);
        //drawPlants(mvpMatrix);

        drawSuares(mvpMatrix);

        levelSign.draw(mvpMatrix, 3);
        signText.draw(mvpMatrix);

        signWood.draw(mvpMatrix, 6);
        aboutText.draw(mvpMatrix);

        ticketStation.draw(mvpMatrix, 5);

        build85.draw(mvpMatrix, 8);
        buddha.draw(mvpMatrix, 16);
        glImageText1.draw(mvpMatrix);

        characterMusic.draw(mvpMatrix, 27);
        glImageText2.draw(mvpMatrix);

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

    public void drawSuares(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        for(GLSquare glSquare : squares) {

            RectF rectF = glSquare.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                glSquare.draw(mvpMatrix);
            }
        }
    }

    public void drawPlants(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        for(Image image : plants) {

            RectF rectF = image.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                image.draw(mvpMatrix, 2);
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

        characterMusic = new Image(new RectF(buddha.getSrcRect().right + plantSize * 4, plantHeight - CHARACTER_MUSIC_HEIGHT,
                buddha.getSrcRect().right + CHARACTER_MUSIC_WIDTH, plantHeight));

        glImageText2 = new GLImageText(context, "My hobby", buddha.getSrcRect().right + plantSize * 4, plantHeight / 6);

        glTrees = new GLTrees(6, scaling, width, height);
    }

    public void createClouds() {

        for(int i = 0 ; i < CLOUD_SIZE ; i++) {

            Random random = new Random();

            float left = random.nextInt((int) (width - CLOUD_WIDTH));
            float top = random.nextInt((int) (height / 2));
            float right = left + CLOUD_WIDTH;
            float bottom = top + CLOUD_HEIGHT;

            clouds.add(new Image(new RectF(left, top, right, bottom)));
        }
    }

    private void createSquare() {
        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        for(int i = 0 ; i < PLANT_RANGE_SIZE ; i++) {

            float left = dstRect.left + plantSize * i;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            squares.add(new GLSquare(new RectF(left, top, right, bottom), new float[] {0, 0, 0, 1.0f}));
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
        ticketStation.computeDstRect(dstRect);
        buddha.computeDstRect(dstRect);
        computeSquare();
        computeClouds();
        //computePlants();
        computeAbout();
        computeLevelSign();
        computeBuild85();
        computeOwnMusic();
    }

    private void computeLevelSign() {

        levelSign.computeDstRect(dstRect);

        signText.setLocation(levelSign.getDstRect().centerX() - signText.getWidth() / 2,
                levelSign.getDstRect().top + signText.getHeight());
    }

    private void computeAbout() {

        signWood.computeDstRect(dstRect);

        aboutText.setLocation(signWood.getDstRect().centerX() - aboutText.getWidth() / 2,
                signWood.getDstRect().top + aboutText.getHeight() / 2);

    }

    private void computeBuild85() {

        build85.computeDstRect(dstRect);

        RectF textSrcRect = glImageText1.getSrcRect();

        glImageText1.setLocation(build85.getDstRect().centerX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
    }

    private void computeOwnMusic() {

        characterMusic.computeDstRect(dstRect);

        RectF textSrcRect = glImageText2.getSrcRect();

        glImageText2.setLocation(characterMusic.getDstRect().centerX() - textSrcRect.width() / 2,
                dstRect.top + textSrcRect.top);
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

    private void computeSquare() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        int count = 0;
        for(GLSquare glSquare : squares) {

            float left = dstRect.left + plantSize * count;
            float top = dstRect.bottom - plantSize;
            float right = left + plantSize;
            float bottom = top + plantSize;

            glSquare.setDstRect(left, top, right, bottom);

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
