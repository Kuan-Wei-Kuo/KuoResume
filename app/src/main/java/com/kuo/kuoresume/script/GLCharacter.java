package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.animation.SampleAnimation;
import com.kuo.kuoresume.animation.SpriteController;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;
import com.kuo.kuoresume.object.RectCollider;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLCharacter extends ComputeRect {

    private static final float CHARACTER_IDLE_UV_BOX_WIDTH = 0.333f;
    private static final float CHARACTER_RUN_UV_BOX_WIDTH = 0.167f;
    private static final float CHARACTER_JUMP_UV_BOX_WIDTH = 0.25f;

    public static final int CHARACTER_IDLE = 0;
    public static final int CHARACTER_RUN = 1;
    public static final int CHARACTER_JUMP = 2;
    public static final int CHARACTER_DOWN = 4;
    public static final int CHARACTER_BOAT = 3;
    public static final int CHARACTER_VEHICLE = 5;

    private float CHARACTER_RUN_WIDTH = 101;
    private float CHARACTER_RUN_HEIGHT = 139;

    private float CHARACTER_BOAT_WIDTH = 142;
    private float CHARACTER_BOAT_HEIGHT = 142;

    private float CHARACTER_JUMP_HEIGHT;

    public int CHARACTER_STATE;

    private SampleAnimation moveAnimation, jumpAnimation, downAnimation;
    private SpriteController characterIdleController, characterRunController, characterJumpController;

    private float moveSpeed = 30;
    private int direction = 1, airDirection = 1;

    private GLImage characterRun, characterIdle, characterBoat, characterJump;

    public GLCharacter(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        CHARACTER_RUN_WIDTH = CHARACTER_RUN_WIDTH * viewComputeListener.getScaling();
        CHARACTER_RUN_HEIGHT = CHARACTER_RUN_HEIGHT * viewComputeListener.getScaling();

        CHARACTER_BOAT_WIDTH = CHARACTER_BOAT_WIDTH * viewComputeListener.getScaling();
        CHARACTER_BOAT_HEIGHT = CHARACTER_BOAT_HEIGHT * viewComputeListener.getScaling();

        CHARACTER_JUMP_HEIGHT = viewComputeListener.getViewCompute().getPlantSize();

        moveSpeed = moveSpeed * viewComputeListener.getScaling();

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        dstRect.set(contentRect.centerX() - CHARACTER_RUN_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_RUN_HEIGHT,
                contentRect.centerX() + CHARACTER_RUN_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize());

        srcRect.set(dstRect);

        characterRunController = new SpriteController(100, 0, 0, 6);
        characterRunController.setOnUpdateListener(deadPoolRunListener);

        characterJumpController = new SpriteController(200, 0, 0, 4);
        characterJumpController.setOnUpdateListener(characterJumpListener);

        characterIdleController = new SpriteController(200, 0, 0, 3);
        characterIdleController.setOnUpdateListener(characterIdleListener);

        characterRun = new GLImage(dstRect);
        characterRun.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        characterIdle = new GLImage(dstRect);
        characterIdle.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        characterBoat = new GLImage(new RectF(contentRect.centerX() - CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_BOAT_HEIGHT,
                contentRect.centerX() + CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize()));
        characterBoat.setDstRect(contentRect.centerX() - CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize() - CHARACTER_BOAT_HEIGHT,
                contentRect.centerX() + CHARACTER_BOAT_WIDTH / 2,
                contentRect.bottom - viewComputeListener.getViewCompute().getPlantSize());

        characterJump = new GLImage(dstRect);
        characterJump.setDstRect(dstRect.left, dstRect.top, dstRect.right, dstRect.bottom);

        moveAnimation = new SampleAnimation(20);
        moveAnimation.setOnUpdateListener(moveAnimationListener);

        jumpAnimation = new SampleAnimation(20);
        jumpAnimation.setOnUpdateListener(jumpAnimationListener);

        downAnimation = new SampleAnimation(20);
        downAnimation.setOnUpdateListener(downAnimationListener);

        CHARACTER_STATE = CHARACTER_IDLE;
    }

    public void draw(float[] mvpMatrix) {

        switch (CHARACTER_STATE) {
            case CHARACTER_IDLE:
                characterIdle.draw(mvpMatrix, 26);
                break;
            case CHARACTER_RUN:
                characterRun.draw(mvpMatrix, 0);
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
                break;
            case CHARACTER_JUMP:
                characterJump.draw(mvpMatrix, 4);
                break;
            case CHARACTER_DOWN:
                characterJump.draw(mvpMatrix, 4);
                break;
            case CHARACTER_VEHICLE:
                characterIdle.draw(mvpMatrix, 26);
                break;
        }

    }



    public void computeSprite(int CHARACTER_STATE) {

        if(CHARACTER_STATE != CHARACTER_DOWN && CHARACTER_STATE != CHARACTER_JUMP && characterJumpController.isEnd() && !characterJumpController.isKeep())
            if(CHARACTER_STATE == CHARACTER_RUN) {
                moveAnimation.start();
                characterRunController.start();
            } else
                characterIdleController.start();
        else {

            if(!characterJumpController.isKeep()) {
                characterJumpController.start();
                moveAnimation.start();
            } else {
                characterJumpController.keepHorizontalFrame(3);
            }

            if(CHARACTER_STATE == CHARACTER_JUMP)
                jumpAnimation.start();

            if(CHARACTER_STATE == CHARACTER_DOWN)
                downAnimation.start();
        }

    }


    private RectCollider.ColliderListener colliderListener = new RectCollider.ColliderListener() {
        @Override
        public void start(RectF dstRect) {

            viewComputeListener.getViewCompute().setFloorHeight(dstRect.height());
            setCharacterState(CHARACTER_JUMP);

        }

        @Override
        public void end(RectF dstRect) {

            if(CHARACTER_STATE != CHARACTER_JUMP)
                setCharacterState(CHARACTER_DOWN);

            viewComputeListener.getViewCompute().setFloorHeight(0);

        }
    };

    private RectCollider.ColliderListener jumpMessage = new RectCollider.ColliderListener() {
        @Override
        public void start(RectF dstRect) {

            viewComputeListener.getViewCompute().setFloorHeight(dstRect.height());
            setCharacterState(CHARACTER_JUMP);
        }

        @Override
        public void end(RectF dstRect) {

            if(CHARACTER_STATE != CHARACTER_JUMP)
                setCharacterState(CHARACTER_JUMP);

            viewComputeListener.getViewCompute().setFloorHeight(0);
        }
    };

    private SampleAnimation.OnUpdateListener moveAnimationListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF curRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            if(characterRun.getDstRect().centerX() == contentRect.centerX()) {

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

        }
    };

    private SampleAnimation.OnUpdateListener jumpAnimationListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            if (characterJumpController.getCurrentHorizontalFrame() > 2 || characterJumpController.isKeep())
                airDirection = -1;
            else
                airDirection = 1;

            float height = currentRect.height();
            float width = currentRect.width();

            float left = currentRect.left;
            float right = left + width;
            float top = currentRect.top + moveSpeed * airDirection;
            float bottom = top + height;

            if (left > contentRect.left) {
                left = contentRect.left;
                right = left + width;
            } else if (right < contentRect.right) {
                right = contentRect.right;
                left = right - width;
            }

            if (top > contentRect.top + CHARACTER_JUMP_HEIGHT) {
                top = contentRect.top + CHARACTER_JUMP_HEIGHT;
                bottom = top + height;

            } else if (contentRect.bottom + viewComputeListener.getViewCompute().getFloorHeight() > bottom && airDirection == -1) {
                bottom = contentRect.bottom + viewComputeListener.getViewCompute().getFloorHeight();
                top = bottom - height;

                characterJumpController.setEnd(true);
                characterJumpController.setKeep(false);
                CHARACTER_STATE = CHARACTER_IDLE;
            }

            currentRect.left = left;
            currentRect.top = top;
            currentRect.right = right;
            currentRect.bottom = bottom;
        }
    };

    private SampleAnimation.OnUpdateListener downAnimationListener = new SampleAnimation.OnUpdateListener() {
        @Override
        public void onUpdate() {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            float height = currentRect.height();
            float width = currentRect.width();

            float left = currentRect.left;
            float right = left + width;
            float top = currentRect.top + moveSpeed * -1;
            float bottom = top + height;

            if (contentRect.bottom + viewComputeListener.getViewCompute().getFloorHeight() > bottom) {

                bottom = contentRect.bottom + viewComputeListener.getViewCompute().getFloorHeight();
                top = bottom - height;

                characterJumpController.setEnd(true);
                characterJumpController.setKeep(false);
                CHARACTER_STATE = CHARACTER_IDLE;

            }

            currentRect.left = left;
            currentRect.top = top;
            currentRect.right = right;
            currentRect.bottom = bottom;
        }
    };


    private SpriteController.OnUpdateListener characterIdleListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {
            float left = currentHorizontalFrame * CHARACTER_IDLE_UV_BOX_WIDTH;
            float right = left + CHARACTER_IDLE_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            if(direction == 1) {
                characterIdle.setUVS(new float[] {
                        left, top,
                        left, bottom,
                        right, bottom,
                        right, top
                });
            } else if(direction == -1) {
                characterIdle.setUVS(new float[] {
                        right, top,
                        right, bottom,
                        left, bottom,
                        left, top,
                });
            }
        }

        @Override
        public void onEnd() {

        }
    };

    private SpriteController.OnUpdateListener deadPoolRunListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            float left = currentHorizontalFrame * CHARACTER_RUN_UV_BOX_WIDTH;
            float right = left + CHARACTER_RUN_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            if(direction == 1) {
                characterRun.setUVS(new float[] {
                        left, top,
                        left, bottom,
                        right, bottom,
                        right, top
                });
            } else if(direction == -1) {
                characterRun.setUVS(new float[] {
                        right, top,
                        right, bottom,
                        left, bottom,
                        left, top,
                });
            }

        }

        @Override
        public void onEnd() {

        }
    };

    private SpriteController.OnUpdateListener characterJumpListener = new SpriteController.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame) {

            float left = currentHorizontalFrame * CHARACTER_JUMP_UV_BOX_WIDTH;
            float right = left + CHARACTER_JUMP_UV_BOX_WIDTH;
            float top = 0;
            float bottom = 1;

            if(direction == 1) {
                characterJump.setUVS(new float[] {
                        left, top,
                        left, bottom,
                        right, bottom,
                        right, top
                });
            } else if(direction == -1) {
                characterJump.setUVS(new float[] {
                        right, top,
                        right, bottom,
                        left, bottom,
                        left, top,
                });
            }

        }

        @Override
        public void onEnd() {

            RectF currentRect = viewComputeListener.getViewCompute().getCurRect();
            RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

            if(contentRect.bottom + viewComputeListener.getViewCompute().getFloorHeight() != currentRect.bottom)
                characterJumpController.keepHorizontalFrame(3);
             else
                CHARACTER_STATE = CHARACTER_IDLE;

        }
    };

    private void characterMove() {

        RectF contentRect = viewComputeListener.getViewCompute().getContentRect();

        float[] widths = {characterIdle.getSrcRect().width(), characterRun.getSrcRect().width()};
        float[] lefts = {characterIdle.getDstRect().left, characterRun.getDstRect().left};

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
                    && characterRun.getDstRect().centerX() > contentRect.centerX()) {
                right = contentRect.centerX() + widths[i] / 2;
                left = right - widths[i];
            } else if(right > contentRect.centerX() + widths[i] / 2
                    && characterRun.getDstRect().centerX() < contentRect.centerX()) {
                left = contentRect.centerX() - widths[i] / 2;
                right = left +  widths[i];
            }

            if (i == 0) {
                characterIdle.setDstRect(left, characterIdle.getSrcRect().top, right, characterRun.getSrcRect().bottom);
                setDstRect(left, srcRect.top, right, srcRect.bottom);
            } else if(i == 1) {
                characterRun.setDstRect(left, characterRun.getSrcRect().top, right, characterRun.getSrcRect().bottom);
                characterJump.setDstRect(left, characterRun.getSrcRect().top, right, characterRun.getSrcRect().bottom);
            }
        }
    }

    @Override
    public void setDstRect(float left, float top, float right, float bottom) {
        super.setDstRect(left, top, right, bottom);

        characterIdle.setDstRect(left, top, right, bottom);
        characterRun.setDstRect(left, top, right, bottom);
        characterJump.setDstRect(left, top, right, bottom);

    }

    public void setDirection(int direction) {
        this.direction = direction;
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

    public RectCollider.ColliderListener getColliderListener() {
        return colliderListener;
    }

    public RectCollider.ColliderListener getJumpMessage() {
        return jumpMessage;
    }

}
