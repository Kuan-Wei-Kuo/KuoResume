package com.kuo.kuoresume.renderer;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.view.MotionEvent;

import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Square;
import com.kuo.kuoresume.script.GLAbout;
import com.kuo.kuoresume.script.GLCharacter;
import com.kuo.kuoresume.script.GLExperience;
import com.kuo.kuoresume.script.GLSetting;
import com.kuo.kuoresume.script.GLSkill;
import com.kuo.kuoresume.until.Until;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kuo on 2016/5/4.
 */
public class InterviewRenderer implements Renderer, ViewComputeListener {

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    // Our screenresolution
    float   mScreenWidth = 1280;
    float   mScreenHeight = 768;

    // Misc
    Context mContext;
    long mLastTime;

    Square mSquare;

    ObjectListener objectListener;

    ViewCompute viewCompute;

    GLSetting glSetting;
    GLAbout glAbout;
    GLSkill glSkill;
    GLExperience glExperience;
    GLCharacter glCharacter;

    public InterviewRenderer(Context context, ObjectListener objectListener) {
        mContext = context;
        mLastTime = System.currentTimeMillis() + 100;
        this.objectListener = objectListener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        GLES20.glClearColor(1, 1, 1, 1);

        viewCompute = new ViewCompute();

        mSquare = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        mScreenWidth = width;
        mScreenHeight = height;

        viewCompute.setPlantSize(Until.dp2px(mContext .getResources().getDisplayMetrics().density, 50));
        viewCompute.setContentRect(new RectF(0, 0, width, height));
        viewCompute.setCurRect(new RectF(0, - height, width, height));

        GLES20.glViewport(0, 0, (int) mScreenWidth, (int) mScreenHeight);

        Matrix.orthoM(mProjectionMatrix, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        float ratio = (float) width / height;

        // Screen to the drawing coordinates
        final float left = 0;
        final float right = width;
        final float bottom = height;
        final float top = 0;
        final float near = 0f;
        final float far = 50f;

        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        glSetting = new GLSetting(11);
        glAbout = new GLAbout(mContext, this, objectListener);
        glSkill = new GLSkill(mContext, this, objectListener);
        glExperience = new GLExperience(mContext, this, objectListener);
        glCharacter = new GLCharacter(mContext, this, objectListener);

        createTexture();

        computeRect();

        glCharacter.computeSprite();
        glAbout.computeRect();
        glSkill.computeRect();
        glExperience.computeRect();
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (touch) {
            computeRect();
            glCharacter.computeSprite();
            glAbout.computeRect();
            glSkill.computeRect();
            glExperience.computeRect();
        }

        mSquare.draw(mMVPMatrix);

        glAbout.draw(mMVPMatrix);

        glSkill.draw(mMVPMatrix);

        glExperience.draw(mMVPMatrix);

        //glCharacter.draw(mMVPMatrix);

    }

    boolean touch;

    public void onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN)
            touch = true;
        else if(event.getAction() == MotionEvent.ACTION_UP)
            touch = false;

        if(event.getX() <= mScreenWidth / 2)
            glCharacter.setDirection(-1);
        else
            glCharacter.setDirection(1);

    }

    public void onPause() {
        /* Do stuff to pause the renderer */
    }

    public void onResume() {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    private void createTexture() {
        glSetting.addTexture(0, objectListener.getHolderBitmap().deadPool_run);
        glSetting.addTexture(1, objectListener.getHolderBitmap().font);
        glSetting.addTexture(2, objectListener.getHolderBitmap().plantSand);
        glSetting.addTexture(3, objectListener.getHolderBitmap().signs);
        glSetting.addTexture(4, objectListener.getHolderBitmap().about);
        glSetting.addTexture(5, objectListener.getHolderBitmap().ticketStation);
        glSetting.addTexture(6, objectListener.getHolderBitmap().signWood);
        glSetting.addTexture(7, objectListener.getHolderBitmap().pinkTag);
        glSetting.addTexture(8, objectListener.getHolderBitmap().build85);
        glSetting.addTexture(9, objectListener.getHolderBitmap().plantSea);
        glSetting.addTexture(10, objectListener.getHolderBitmap().videoTape);
    }

    private void computeRect() {

        RectF currentRect = viewCompute.getCurRect();

        float[] widths = {glAbout.getWidth(), glSkill.getWidth(), glExperience.getWidth()};
        float[] heights = {glAbout.getHeight(), glSkill.getHeight(), glExperience.getHeight()};

        float left, top, right = 0, bottom;

        for(int i = 0 ; i < widths.length ; i++) {

            left = i > 0 ? right : currentRect.left;
            top = currentRect.top;
            right = left + widths[i];
            bottom = top + heights[i];

            if (i == 0)
                glAbout.setDstRect(left, top + heights[i], right, bottom + heights[i]);
            else if(i == 1)
                glSkill.setDstRect(left, top, right, bottom);
            else if(i == 2)
                glExperience.setDstRect(left, top + heights[i], right, bottom + heights[i]);
        }
    }

    @Override
    public ViewCompute getViewCompute() {
        return viewCompute;
    }

    @Override
    public GLCharacter getGLCharacter() {
        return glCharacter;
    }
}
