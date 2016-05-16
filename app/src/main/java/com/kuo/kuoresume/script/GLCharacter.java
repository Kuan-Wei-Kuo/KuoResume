package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SpriteAnimation;
import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Image;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLCharacter extends ComputeRect{

    private static final float OWN_IDLE_UV_BOX_WIDTH = 1f;
    private static final float CHARACTER_RUN_UV_BOX_WIDTH = 0.2f;

    public static final int CHARACTER_IDLE = 0;
    public static final int CHARACTER_RUN = 1;
    public static final int CHARACTER_JUMP = 2;
    public static final int CHARACTER_UP = 3;
    public static final int CHARACTER_DOWN = 4;
    public static final int CHARACTER_BOAT = 5;

    private float CHARACTER_RUN_WIDTH = 86;
    private float CHARACTER_RUN_HEIGHT = 142;

    private float CHARACTER_BOAT_WIDTH = 142;
    private float CHARACTER_BOAT_HEIGHT = 142;

    public int CHARACTER_STATE;

    private SpriteAnimation ownAirAnimation;
    private SpriteController deadPoolIdleController, characterRunController, deadPoolDownController, deadPoolUpController;

    private int moveSpeed = 70;
    private int direction = 1, airDirection = 1;

    private Image character_run, character_idle, characterBoat;

    public GLCharacter(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        CHARACTER_RUN_WIDTH = CHARACTER_RUN_WIDTH * viewComputeListener.getScaling();
        CHARACTER_RUN_HEIGHT = CHARACTER_RUN_HEIGHT * viewComputeListener.getScaling();

        CHARACTER_BOAT_WIDTH = CHARACTER_BOAT_WIDTH * viewComputeListener.getScaling();
        CHARACTER_BOAT_HEIGHT = CHARACTER_BOAT_HEIGHT * viewComputeListener.getScaling();

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        dstRect.set(contentRect.centerX() - CHARACTER_RUN_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_RUN_HEIGHT,
                contentRect.centerX() + CHARACTER_RUN_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize());

        characterRunController = new SpriteController(100,
                objectListener.getHolderBitmap().deadPool_run.getWidth() / 2,
                objectListener.getHolderBitmap().deadPool_run.getHeight(), 5);
        characterRunController.setOnUpdateListener(deadPoolRunListener);

        deadPoolDownController = new SpriteController(10,
                objectListener.getHolderBitmap().deadPool_down.getWidth() / 2,
                objectListener.getHolderBitmap().deadPool_down.getHeight(), 2);
        deadPoolDownController.setOnUpdateListener(deadPoolDownListener);

        character_run = new Image(dstRect);
        character_run.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        character_idle = new Image(dstRect);
        character_idle.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        characterBoat = new Image(new RectF(contentRect.centerX() - CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_BOAT_HEIGHT,
                contentRect.centerX() + CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize()));
        characterBoat.setDstRect(contentRect.centerX() - CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_BOAT_HEIGHT,
                contentRect.centerX() + CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize());

        CHARACTER_STATE = CHARACTER_IDLE;
    }

    public void draw(float[] mvpMatrix) {

        switch (CHARACTER_STATE) {
            case CHARACTER_IDLE:
                character_idle.draw(mvpMatrix, 27);
                setDstRect(character_idle.getDstRect());
                break;
            case CHARACTER_RUN:
                character_run.draw(mvpMatrix, 0);
                setDstRect(character_run.getDstRect());
                break;
            case CHARACTER_UP:
                character_idle.draw(mvpMatrix, 27);
                setDstRect(character_idle.getDstRect());
                break;
            case CHARACTER_DOWN:
                character_idle.draw(mvpMatrix, 27);
                setDstRect(character_idle.getDstRect());
                break;
            case CHARACTER_BOAT:
                if(direction == 1)
                    characterBoat.setUVS(new float[] {
                            0.0f, 0.0f,
                            0.0f, 1.0f,
                            1.0f, 1.0f,
                            1.0f, 0.0f,
                    });
                else if(direction == -1)
                    characterBoat.setUVS(new float[] {
                            1.0f, 0.0f,
                            1.0f, 1.0f,
                            0.0f, 1.0f,
                            0.0f, 0.0f,
                    });
                characterBoat.draw(mvpMatrix, 29);
                setDstRect(characterBoat.getDstRect());
                break;
        }
    }

    public void computeSprite(int CHARACTER_STATE) {

        switch (CHARACTER_STATE) {
            case CHARACTER_RUN:
                characterRunController.start();
                break;
            case CHARACTER_UP:
                deadPoolDownController.start();
                break;
            case CHARACTER_DOWN:
                deadPoolDownController.start();
                break;
        }
    }

    private SpriteController.OnUpdateListener deadPoolRunListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF curRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            if(character_run.getDstRect().centerX() == contentRect.centerX()) {

                float width = curRect.width();

                float curLeft = curRect.left - moveSpeed * direction;
                float curRight = curLeft + width;

                if(curLeft > contentRect.left) {
                    curLeft = contentRect.left;
                    curRight = curLeft + width;

                    characterMove();
                } else if(curRight < contentRect.right) {
                    curRight = contentRect.right;
                    curLeft = contentRect.right - width;

                    characterMove();
                }

                curRect.left = curLeft;
                curRect.right = curRight;

            } else {
                characterMove();
            }

            float left = currentHorizontalFrame * CHARACTER_RUN_UV_BOX_WIDTH;
            float right = left + CHARACTER_RUN_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            if(direction == 1) {
                character_run.setUVS(new float[] {
                        left, top,
                        left, bottom,
                        right, bottom,
                        right, top
                });
            } else if(direction == -1) {
                character_run.setUVS(new float[] {
                        right, top,
                        right, bottom,
                        left, bottom,
                        left, top,
                });
            }

        }
    };

    private SpriteController.OnUpdateListener deadPoolDownListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            float height = currentRect.height();

            float top = currentRect.top + moveSpeed * airDirection;
            float bottom = top + height;

            if(top > contentRect.top) {
                top = contentRect.top;
                bottom = top + height;
            } else if(contentRect.bottom > bottom) {
                bottom = contentRect.bottom;
                top = bottom - height;
            }

            currentRect.top = top;
            currentRect.bottom = bottom;
        }
    };

    private void characterMove() {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        float[] widths = {character_idle.getSrcRect().width(), character_run.getSrcRect().width(), characterBoat.getSrcRect().width()};
        float[] lefts = {character_idle.getDstRect().left, character_run.getDstRect().left, characterBoat.getDstRect().left};

        for(int i = 0 ; i < widths.length ; i++) {

            float left = lefts[i] + moveSpeed * direction;
            float right = left + widths[i];

            if(left < contentRect.left) {
                left = contentRect.left;
                right = left +  widths[i];
            } else if (right > contentRect.right) {
                right = contentRect.right;
                left = contentRect.right - widths[i];
            } else if (left < contentRect.centerX() - widths[i] / 2
                    && character_run.getDstRect().centerX() > contentRect.centerX()) {
                right = contentRect.centerX() + widths[i] / 2;
                left = right - widths[i];
            } else if(right > contentRect.centerX() + widths[i] / 2
                    && character_run.getDstRect().centerX() < contentRect.centerX()) {
                left = contentRect.centerX() - widths[i] / 2;
                right = left +  widths[i];
            }

            if (i == 0)
                character_idle.setDstRect(left, character_idle.getSrcRect().top, right, character_run.getSrcRect().bottom);
            else if(i == 1)
                character_run.setDstRect(left, character_run.getSrcRect().top, right, character_run.getSrcRect().bottom);
            else if(i == 2)
                characterBoat.setDstRect(left, characterBoat.getSrcRect().top, right, characterBoat.getSrcRect().bottom);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setAirDirection(int airDirection) {
        this.airDirection = airDirection;
    }

    public void setCharacterState(int state) {
        CHARACTER_STATE = state;
    }

    public int getDirection() {
        return direction;
    }

    public int getCharacterState() {
        return CHARACTER_STATE;
    }
}
