package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ActivityListener;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.renderer.InterviewRenderer;
import com.kuo.kuoresume.script.Human;

/**
 * Created by Kuo on 2016/5/4.
 */
public class GLResumeView extends GLSurfaceView implements ObjectListener {

    private InterviewRenderer mInterviewRenderer;

    private HolderBitmap holderBitmap;

    public GLResumeView(Context context, ActivityListener activityListener) {
        super(context);

        createBitmap();
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        Log.d("createView", "true");
    }

    public GLResumeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        createBitmap();
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);
        Log.d("createView", "true");

    }

    public void setGLRenderer(Context context, ActivityListener activityListener) {
        mInterviewRenderer = new InterviewRenderer(context, this, activityListener);
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

        holderBitmap.deadPool_run = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.own_run);
        holderBitmap.deadPool_idle = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.own_idle);
        holderBitmap.deadPool_down = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_down);
        holderBitmap.spider_deadpool = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.spider_deadpool);
        holderBitmap.own_music = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.own_music);
        holderBitmap.characterBoat = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.own_boat);

        holderBitmap.spiderHead = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.spider_head);
        holderBitmap.deadPoolHead = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.deadpool_head);

        holderBitmap.videoTape = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.videotape);

        holderBitmap.font = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.font);

        holderBitmap.signWood = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.sign_wood);
        holderBitmap.cloud = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.cloud);
        holderBitmap.tree_1 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_1);
        holderBitmap.tree_2 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_2);
        holderBitmap.tree_3 = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.tree_3);
        holderBitmap.buddha = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.buddha);

        holderBitmap.my_chart_lib = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.my_chart_lib_icon);
        holderBitmap.urcoco = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.urcoco_icon);
        holderBitmap.firstaid = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.firstaid);
        holderBitmap.my_logdown = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logdown_main);
        holderBitmap.basketball_board = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.basketball_board_icon);
        holderBitmap.gmail_icon = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.logo_gmail_128px);
        holderBitmap.share_icon = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.share_icon);
        holderBitmap.github_icon = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.github_logo);
        holderBitmap.gold_box = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.gold_box);

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
