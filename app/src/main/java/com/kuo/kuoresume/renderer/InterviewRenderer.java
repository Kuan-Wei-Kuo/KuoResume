package com.kuo.kuoresume.renderer;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;
import com.kuo.kuoresume.object.GLSquare;
import com.kuo.kuoresume.presents.ImageDefaultSize;
import com.kuo.kuoresume.presents.ViewCompute;
import com.kuo.kuoresume.script.GLAbout;
import com.kuo.kuoresume.script.GLBackground;
import com.kuo.kuoresume.script.GLCharacter;
import com.kuo.kuoresume.script.GLExperience;
import com.kuo.kuoresume.script.GLInterview;
import com.kuo.kuoresume.script.GLMessage;
import com.kuo.kuoresume.script.GLSetting;
import com.kuo.kuoresume.script.GLSkill;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * Created by Kuo on 2016/5/4.
 */
public class InterviewRenderer implements Renderer, ViewComputeListener {

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private boolean isTouch = false;
    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 768;
    float 	ssu = 1.0f;
    float 	ssx = 1.0f;
    float 	ssy = 1.0f;
    float 	swp = 320.0f;
    float 	shp = 480.0f;

    Context mContext;
    long mLastTime;

    ViewCompute viewCompute;

    GLSquare glSquare;

    GLImage bg;

    GLBackground glBackground, glBackground_1;
    GLSetting glSetting;
    GLInterview glInterview;
    GLAbout glAbout;
    GLSkill glSkill;
    GLExperience glExperience;
    GLCharacter glCharacter;
    GLMessage glMessage;
    ActivityListener activityListener;

    boolean isFirstDraw = true;

    public InterviewRenderer(Context context, ActivityListener activityListener) {
        mContext = context;
        mLastTime = System.currentTimeMillis() + 100;
        this.activityListener = activityListener;

        viewCompute = new ViewCompute();

        //activityListener.showProgress();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glSetting = new GLSetting(mContext, 31);

        Log.d("isFirst", isFirstDraw + "");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        mScreenWidth = width;
        mScreenHeight = height;

        computeScaling();

        viewCompute.setPlantSize(ImageDefaultSize.PLANT_SIZE * ssu);
        viewCompute.setContentRect(new RectF(0, 0, width, height));
        viewCompute.setCurRect(new RectF(0, -height, width, height));

        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        Matrix.orthoM(mProjectionMatrix, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        //float ratio = (float) width / height;

        // Screen to the drawing coordinates
        final float left = 0;
        final float right = mScreenWidth;
        final float bottom = mScreenHeight;
        final float top = 0;
        final float near = 0f;
        final float far = 50f;

        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        glBackground = new GLBackground(mContext, this);
        glBackground_1 = new GLBackground(mContext, this);

        glSquare = new GLSquare(new RectF(0, 500, 200, 700), new float[] {1, 0.5f, 1, 1});

        glCharacter = new GLCharacter(mContext, this);
        glInterview = new GLInterview(mContext, this);
        glAbout = new GLAbout(mContext, this);
        glSkill = new GLSkill(mContext, this);
        glExperience = new GLExperience(mContext, this);
        glMessage = new GLMessage(mContext, this, activityListener);

        computeCurrentRect();
        computeRect();

        //activityListener.dismissProgrees();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        if(isFirstDraw) {
            isFirstDraw = false;
            onRendererListener.onFirstDraw();
        }

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if(glCharacter.getCharacterState() == GLCharacter.CHARACTER_IDLE)
            glCharacter.computeSprite(GLCharacter.CHARACTER_IDLE);

        if(isTouch || glCharacter.getCharacterState() == GLCharacter.CHARACTER_JUMP
                || glCharacter.getCharacterState() == GLCharacter.CHARACTER_DOWN) {

            if(isTouch && glCharacter.getCharacterState() != GLCharacter.CHARACTER_JUMP
                    && glCharacter.getCharacterState() != GLCharacter.CHARACTER_DOWN)
                glCharacter.setCharacterState(GLCharacter.CHARACTER_RUN);

            glCharacter.computeSprite(glCharacter.getCharacterState());
            computeRect();
        }

        bg.draw(mMVPMatrix, 5);

        glInterview.draw(mMVPMatrix);

        glAbout.draw(mMVPMatrix);

        glSkill.draw(mMVPMatrix);

        glExperience.draw(mMVPMatrix);

        glMessage.draw(mMVPMatrix);

        glCharacter.draw(mMVPMatrix);
    }

    PointF pointF = new PointF(0, 0);

    public void onTouchEvent(MotionEvent event) {

        if(!glMessage.onTouchContact(event) && !glMessage.isTactFocus()
                && !glMessage.onTouchShare(event) && !glMessage.isShareFocus()
                && !glMessage.onTouchGithub(event) && !glMessage.isGithubFocus()) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                pointF.set(event.getX(), event.getY());

                if(glCharacter.getCharacterState() != GLCharacter.CHARACTER_JUMP
                        && glCharacter.getCharacterState() != GLCharacter.CHARACTER_DOWN)
                    glCharacter.setCharacterState(GLCharacter.CHARACTER_RUN);

                isTouch = true;

            } else if(event.getAction() == MotionEvent.ACTION_MOVE) {

                //if(Math.abs(pointF.y - event.getY()) > 500)
                    //glCharacter.setCharacterState(GLCharacter.CHARACTER_JUMP);

            } else if(event.getAction() == MotionEvent.ACTION_UP) {

                if(glCharacter.getCharacterState() == GLCharacter.CHARACTER_BOAT)
                    glCharacter.setCharacterState(GLCharacter.CHARACTER_BOAT);
                else if(glCharacter.getCharacterState() == GLCharacter.CHARACTER_JUMP)
                    glCharacter.setCharacterState(GLCharacter.CHARACTER_JUMP);
                else if(glCharacter.getCharacterState() == GLCharacter.CHARACTER_DOWN)
                    glCharacter.setCharacterState(GLCharacter.CHARACTER_DOWN);
                else
                    glCharacter.setCharacterState(GLCharacter.CHARACTER_IDLE);

                isTouch = false;

            }

            if(event.getX() <= mScreenWidth / 2)
                glCharacter.setDirection(-1);
            else
                glCharacter.setDirection(1);

        }

    }

    @Override
    public void computeRect() {

        RectF currentRect = viewCompute.getCurRect();

        float[] widths = {glInterview.getWidth(), glAbout.getWidth(), glSkill.getWidth(), glExperience.getWidth(), glMessage.getWidth()};
        float[] heights = {glInterview.getHeight(), glAbout.getHeight(), glSkill.getHeight(), glExperience.getHeight(), glMessage.getHeight()};
        float[] tops = {glInterview.getSrcRect().top, glAbout.getSrcRect().top, glSkill.getSrcRect().top, glExperience.getSrcRect().top, glMessage.getSrcRect().top};

        float left, top, right = 0, bottom;

        for(int i = 0 ; i < widths.length ; i++) {

            left = i > 0 ? right : currentRect.left;
            top = currentRect.top + tops[i];
            right = left + widths[i];
            bottom = top + heights[i];

            if (i == 0)
                glInterview.setDstRect(left, top, right, bottom);
            else if (i == 1)
                glAbout.setDstRect(left, top, right, bottom);
            else if(i == 2)
                glSkill.setDstRect(left, top, right, bottom);
            else if(i == 3)
                glExperience.setDstRect(left, top, right, bottom);
            else if(i == 4)
                glMessage.setDstRect(left, top, right, bottom);
        }

        bg.computeDstRect(currentRect);

        glBackground.setDstRect(currentRect.left,
                currentRect.top,
                currentRect.left + glBackground.getWidth(),
                currentRect.bottom);

        glBackground.computeRect();

        glBackground_1.setDstRect(glBackground.getDstRect().right,
                currentRect.top,
                glBackground.getDstRect().right + glBackground_1.getWidth(),
                currentRect.bottom);

        glBackground_1.computeRect();
        glInterview.computeRect();
        glAbout.computeRect();
        glSkill.computeRect();
        glExperience.computeRect();
        glMessage.computeRect();
    }

    private void computeCurrentRect() {

        float[] widths = {glInterview.getWidth(), glAbout.getWidth(), glSkill.getWidth(), glExperience.getWidth(), glMessage.getWidth()};
        float[] heights = {glInterview.getHeight(), glAbout.getHeight(), glSkill.getHeight(), glExperience.getHeight(), glMessage.getHeight()};

        float totalWidth = 0;
        float maxHeight = 0;

        int count = 0;
        for(float f : widths) {
            totalWidth += f;
            maxHeight = Math.max(maxHeight, heights[count]);
            count++;
        }

        if(viewCompute.getCacheRect() != null)
            viewCompute.setCurRect(viewCompute.getCacheRect());
        else
            viewCompute.setCurRect(new RectF(0, mScreenHeight - maxHeight, totalWidth, mScreenHeight));

        glInterview.setSrcRect(0, viewCompute.getCurRect().height() - glInterview.getHeight(), glInterview.getWidth(), glInterview.getHeight());
        glAbout.setSrcRect(0, viewCompute.getCurRect().height() - glAbout.getHeight(), glAbout.getWidth(), glAbout.getHeight());
        glSkill.setSrcRect(0, viewCompute.getCurRect().height() - glSkill.getHeight(), glSkill.getWidth(), glSkill.getHeight());
        glExperience.setSrcRect(0, viewCompute.getCurRect().height() - glExperience.getHeight(), glExperience.getWidth(), glExperience.getHeight());
        glMessage.setSrcRect(0, viewCompute.getCurRect().height() - glMessage.getHeight(), glMessage.getWidth(), glMessage.getHeight());

        bg = new GLImage(new RectF(0, 0, viewCompute.getCurRect().width(), viewCompute.getCurRect().height()));
        bg.computeDstRect(viewCompute.getCurRect());
    }

    private void computeScaling() {
        // The screen resolutions
        swp = mContext.getResources().getDisplayMetrics().widthPixels;
        shp = mContext.getResources().getDisplayMetrics().heightPixels;

        // Orientation is assumed portrait
        ssx = swp / 320.0f;
        ssy = shp / 480.0f;

        // Get our uniform scaler
        if(ssx > ssy)
            ssu = ssy;
        else
            ssu = ssx;
    }

    @Override
    public float getScaling() {
        return ssu;
    }

    @Override
    public ViewCompute getViewCompute() {
        return viewCompute;
    }

    @Override
    public GLCharacter getGLCharacter() {
        return glCharacter;
    }

    public void onPause() {
        /* Do stuff to pause the renderer */
        viewCompute.setCacheRect(viewCompute.getCurRect());
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        isFirstDraw = true;
        mLastTime = System.currentTimeMillis();
    }

    public interface OnRendererListener {
        void onFirstDraw();
    }

    private OnRendererListener onRendererListener;

    public void setOnGLViewListener(OnRendererListener onRendererListener) {
        this.onRendererListener = onRendererListener;
    }
}
