package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.renderer.InterviewRenderer;
import com.kuo.kuoresume.script.Human;

/**
 * Created by Kuo on 2016/5/4.
 */
public class GLResumeView extends GLSurfaceView implements ObjectListener {

    private InterviewRenderer mInterviewRenderer;

    private HolderBitmap holderBitmap;

    public GLResumeView(Context context) {
        super(context);

        createBitmap();
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mInterviewRenderer = new InterviewRenderer(context, this);
        setRenderer(mInterviewRenderer);
    }

    private void createBitmap() {
        holderBitmap = new HolderBitmap();
        holderBitmap.plantSand = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.plant_1);
        holderBitmap.plantSea = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.sea);
        holderBitmap.sand = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.sand);
        holderBitmap.signs = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tag);
        holderBitmap.woodTag = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tag_1);
        holderBitmap.pinkTag = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tag_2);
        holderBitmap.blueTag = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tag_3);
        holderBitmap.blueStamp = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.picture);
        holderBitmap.redStamp = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.red_stamp);
        holderBitmap.build85 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.build_85);
        holderBitmap.ticketStation = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.ticket);
        holderBitmap.about = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.about);
        holderBitmap.skill = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.skill);
        holderBitmap.exprience = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.experience);

        holderBitmap.deadPool_run = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_run);
        holderBitmap.deadPool_idle = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_idle);
        holderBitmap.deadPool_down = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_down);
        holderBitmap.spider_deadpool = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.spider_deadpool);

        holderBitmap.spiderHead = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.spider_head);
        holderBitmap.deadPoolHead = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_head);

        holderBitmap.videoTape = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.videotape);

        holderBitmap.font = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.font);

        holderBitmap.signWood = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.sign_wood);
        holderBitmap.cloud = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.cloud);
        holderBitmap.tree_1 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_1);
        holderBitmap.tree_2 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_2);
        holderBitmap.tree_3 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mInterviewRenderer.onTouchEvent(event);
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        mInterviewRenderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mInterviewRenderer.onResume();
    }

    @Override
    public HolderBitmap getHolderBitmap() {
        return holderBitmap;
    }

    @Override
    public ViewCompute getViewCompute() {
        return null;
    }

    @Override
    public Human getHuman() {
        return null;
    }
}
