package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SampleAnimation;
import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.compute.ImageDefaultSize;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;
import com.kuo.kuoresume.object.GLImageText;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.object.GLTrees;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLAbout extends ComputeRect {

    private static final float BUILD_IMAGE_UV_BOX_WIDTH = 0.5f;
    private static final float COFFEE_IMAGE_UV_BOX_WIDTH = 0.25f;
    private static final float MUSIC_ICON_UV_BOX_WIDTH = 0.25f;
    private static final float OWN_MUSIC_UV_BOX_WIDTH = 0.5f;

    private static final int OBSTACLE_HEIGHT = 2;
    private static final int OBSTACLE_WIDTH = 4;

    private static final int COFFEE_WIDTH = 230;
    private static final int COFFEE_HEIGHT = 230;

    private float CHARACTER_MUSIC_WIDTH = 168;
    private float CHARACTER_MUSIC_HEIGHT = 250;

    private float MUSIC_ICON_WIDTH = 128;
    private float MUSIC_ICON_HEIGHT = 128;

    public float MUSIC_ICONS_MOVE_SPEED = 5;

    private float OFFICE_WIDTH = 800;
    private float OFFICE_HEIGHT = 600;

    private ArrayList<GLSquare> squares = new ArrayList<>();

    private ArrayList<GLImage> musicIcons = new ArrayList<>();

    private ArrayList<SampleAnimation> musicIconAnimations = new ArrayList<>();

    private SpriteController ownMusicSpriteController;

    private GLImage build85, buddha, characterMusic, aboutCoffee, officeComputer;

    private GLImageText glImageText1, glImageText2;

    private GLSquare obstacle_1, obstacle_2;

    private GLTrees glTrees;

    private int musicAnimationCount = 0;

    public GLAbout(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 45;

        OFFICE_WIDTH = OFFICE_WIDTH * viewComputeListener.getScaling();
        OFFICE_HEIGHT = OFFICE_HEIGHT * viewComputeListener.getScaling();

        CHARACTER_MUSIC_WIDTH = CHARACTER_MUSIC_WIDTH * viewComputeListener.getScaling();
        CHARACTER_MUSIC_HEIGHT = CHARACTER_MUSIC_HEIGHT * viewComputeListener.getScaling();

        MUSIC_ICON_WIDTH = MUSIC_ICON_WIDTH * viewComputeListener.getScaling();
        MUSIC_ICON_HEIGHT = MUSIC_ICON_HEIGHT * viewComputeListener.getScaling();

        MUSIC_ICONS_MOVE_SPEED = MUSIC_ICONS_MOVE_SPEED * viewComputeListener.getScaling();

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

        ownMusicSpriteController = new SpriteController(700, 0, 0, 2);
        ownMusicSpriteController.setOnUpdateListener(onUpdateListener);

        createSquare();
        createImages();
        createMusicIcons();

        setDstRect(0, 0, width, height);

    }

    private void createMusicIcons() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        for(int i = 0 ; i < 4 ; i++) {

            float left = plantSize * 27 + (MUSIC_ICON_WIDTH + MUSIC_ICON_WIDTH * 0.3f) * i;
            float top = plantHeight - MUSIC_ICON_HEIGHT;
            float right = left + MUSIC_ICON_WIDTH;
            float bottom = top + MUSIC_ICON_HEIGHT;

            if(i > 1) {
                left += CHARACTER_MUSIC_WIDTH;
                right = left + MUSIC_ICON_WIDTH;
            }

            GLImage glImage = new GLImage(new RectF(left, top, right, bottom));
            glImage.setUVS(new float[] {MUSIC_ICON_UV_BOX_WIDTH * i, 0,
                    MUSIC_ICON_UV_BOX_WIDTH * i, 1,
                    MUSIC_ICON_UV_BOX_WIDTH * i + MUSIC_ICON_UV_BOX_WIDTH, 1,
                    MUSIC_ICON_UV_BOX_WIDTH * i + MUSIC_ICON_UV_BOX_WIDTH, 0});

            musicIcons.add(glImage);

            SampleAnimation sampleAnimation = new SampleAnimation(25);
            sampleAnimation.setOnUpdateListener(onMusicIconListener);

            musicIconAnimations.add(sampleAnimation);
        }

        characterMusic = new GLImage(new RectF(musicIcons.get(1).getSrcRect().right, plantHeight - CHARACTER_MUSIC_HEIGHT,
                musicIcons.get(1).getSrcRect().right + CHARACTER_MUSIC_WIDTH, plantHeight));

        glImageText2 = new GLImageText(context, "My hobby", 0, plantHeight / 6);

    }

    private void createImages() {

        float plantSize = viewComputeListener.getViewCompute().getPlantSize();

        float plantHeight = height - plantSize;

        float scaling = viewComputeListener.getScaling();

        officeComputer = new GLImage(new RectF(plantSize, height - OFFICE_HEIGHT,
                plantSize + OFFICE_WIDTH, height));

        officeComputer.computeDstRect(dstRect);

        aboutCoffee = new GLImage(new RectF(plantSize * 2,
                plantHeight + COFFEE_HEIGHT * 0.1f - COFFEE_HEIGHT * scaling,
                plantSize * 2 + COFFEE_WIDTH * scaling,
                plantHeight + COFFEE_HEIGHT * 0.1f ));

        aboutCoffee.setUVS(new float[] {
                0, 0,
                0, 1,
                COFFEE_IMAGE_UV_BOX_WIDTH, 1,
                COFFEE_IMAGE_UV_BOX_WIDTH, 0
        });

        obstacle_1 = new GLSquare(new RectF(plantSize * 8,
                plantHeight - plantSize * OBSTACLE_HEIGHT,
                plantSize * 8 + plantSize * OBSTACLE_WIDTH,
                plantHeight), new float[] {0, 0, 0, 1});

        build85 = new GLImage(new RectF(plantSize * 14,
                plantHeight - ImageDefaultSize.BUILD85_HEIGHT * scaling,
                plantSize * 14 + ImageDefaultSize.BUILD85_WIDTH * scaling, plantHeight));

        build85.setUVS(new float[] {
                0, 0,
                0, 1,
                BUILD_IMAGE_UV_BOX_WIDTH, 1,
                BUILD_IMAGE_UV_BOX_WIDTH, 0
        });

        buddha = new GLImage(new RectF(plantSize * 18,
                plantHeight - ImageDefaultSize.BUILD85_HEIGHT * scaling,
                plantSize * 18 + ImageDefaultSize.BUILD85_WIDTH * scaling,
                plantHeight));

        buddha.setUVS(new float[] {
                BUILD_IMAGE_UV_BOX_WIDTH, 0,
                BUILD_IMAGE_UV_BOX_WIDTH, 1,
                1, 1,
                1, 0
        });

        obstacle_2 = new GLSquare(new RectF(plantSize * 22,
                plantHeight - plantSize * OBSTACLE_HEIGHT,
                plantSize * 22 + plantSize * OBSTACLE_WIDTH,
                plantHeight), new float[] {0, 0, 0, 1});

        obstacle_1.setColliderListener(viewComputeListener.getGLCharacter().getColliderListener());
        obstacle_2.setColliderListener(viewComputeListener.getGLCharacter().getColliderListener());

        glImageText1 = new GLImageText(context, "Live in Kaohsiung City", plantSize * 14, plantHeight / 6);

        glTrees = new GLTrees(6, scaling, width, height);
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

    public void draw(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        if(dstRect.contains(contentRect.left, contentRect.top)
                || dstRect.contains(contentRect.left, contentRect.bottom)
                || dstRect.contains(contentRect.right, contentRect.top)
                || dstRect.contains(contentRect.right, contentRect.bottom)) {
            musicAnimationCount = 0;

            for (SampleAnimation sampleAnimation : musicIconAnimations) {
                sampleAnimation.start();
                musicAnimationCount++;
            }

            for (GLImage glImage : musicIcons)
                glImage.computeDstRect(dstRect);

            ownMusicSpriteController.start();

            officeComputer.draw(mvpMatrix, 12);

            drawSquares(mvpMatrix);

            aboutCoffee.draw(mvpMatrix, 16);

            obstacle_1.draw(mvpMatrix);

            build85.draw(mvpMatrix, 8);

            buddha.draw(mvpMatrix, 8);

            obstacle_2.draw(mvpMatrix);

            glImageText1.draw(mvpMatrix);

            drawMusicIcons(mvpMatrix);
            characterMusic.draw(mvpMatrix, 27);
            glImageText2.draw(mvpMatrix);
        }

    }

    private void drawSquares(float[] mvpMatrix) {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        for(GLSquare glSquare : squares) {

            RectF rectF = glSquare.getDstRect();

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1)) {
                glSquare.draw(mvpMatrix);
            }
        }
    }

    private void drawMusicIcons(float[] m) {

        for (GLImage glImage : musicIcons)
            glImage.draw(m, 22);

    }

    public void computeRect() {

        glTrees.computeTrees(dstRect);

        buddha.computeDstRect(dstRect);

        aboutCoffee.computeDstRect(dstRect);

        obstacle_1.computeDstRect(dstRect);
        obstacle_2.computeDstRect(dstRect);

        obstacle_1.startCollider(viewComputeListener.getGLCharacter().getDstRect());
        obstacle_2.startCollider(viewComputeListener.getGLCharacter().getDstRect());

        officeComputer.computeDstRect(dstRect);

        computeSquare();
        computeBuild85();
        computeOwnMusic();
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

    private int musicIconsDirection = 1;

    private SampleAnimation.OnUpdateListener onMusicIconListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF srcRect = musicIcons.get(musicAnimationCount).getSrcRect();
            RectF dstRect = musicIcons.get(musicAnimationCount).getDstRect();

            float height = srcRect.height();

            float top = dstRect.top - MUSIC_ICONS_MOVE_SPEED * musicIconsDirection;
            float bottom = top + height;

            if(top < characterMusic.getSrcRect().top) {
                top = characterMusic.getSrcRect().top;
                bottom = top + height;
                musicIconsDirection = -1;
            } else if (bottom > characterMusic.getSrcRect().bottom) {
                bottom = srcRect.bottom;
                top = bottom - height;
                musicIconsDirection = 1;
            }

            musicIcons.get(musicAnimationCount).setSrcRect(srcRect.left, top, srcRect.right, bottom);
        }
    };
}
