package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.script.About;
import com.kuo.kuoresume.script.Experience;
import com.kuo.kuoresume.script.Human;
import com.kuo.kuoresume.script.Interview;
import com.kuo.kuoresume.script.Skill;
import com.kuo.kuoresume.until.Until;

/*
 * Created by Kuo on 2016/4/18.
 */
public class ResumeView extends AbsResumeView implements ObjectListener{

    private Paint textPaint;

    private Human humanObj;

    private ViewCompute viewCompute;

    private HolderBitmap holderBitmap;

    private Interview mInterview;

    private About mAbout;

    private Skill mSkill;

    private Experience mExperience;

    public ResumeView(Context context) {
        this(context, null, 0);
    }

    public ResumeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResumeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "crushed_text.ttf"));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(Until.dp2px(getContext().getResources().getDisplayMetrics().density, 18));

    }
    long startFrameTime;
    @Override
    public void onResumeDraw(Canvas canvas) {

        startFrameTime = System.currentTimeMillis();

        updateTouchEvent();
        computeRect();

        drawBackground(canvas);

        mInterview.draw(canvas);

        mAbout.drawAbout(canvas, viewCompute, holderBitmap, humanObj);

        mSkill.draw(canvas);

        mExperience.draw(canvas);

        humanObj.drawHuman(canvas);

        drawFPS(canvas);

    }

    @Override
    public boolean onLoad(int width, int height) {

        if(!isLoaded) {

            createBitmap();

            viewCompute = new ViewCompute();
            viewCompute.setPlantSize(Until.dp2px(getContext().getResources().getDisplayMetrics().density, 50));
            viewCompute.setContentRect(new RectF(0, 0, width, height));

            mInterview = new Interview(getContext(), width, height, this);
            mAbout = new About(getContext(), this);
            mSkill = new Skill(getContext(), this);
            mExperience = new Experience(getContext(), this);

            prepareViewCompute(width, height);

            humanObj = new Human(getContext(),
                        new Rect(width / 2 - (int) viewCompute.getPlantSize() / 2,
                                height - (int) viewCompute.getPlantSize() * 3,
                                width / 2 + (int) viewCompute.getPlantSize() / 2,
                                height - (int) viewCompute.getPlantSize()), this);

            isLoaded = true;
        }

        return isLoaded;
    }

    private int state = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(humanObj.getState() != Human.HUMAN_BAT) {

            if(event.getAction() == MotionEvent.ACTION_DOWN) {

                if(event.getX() <= getWidth() / 2)
                    humanObj.setDirection(-1);
                else
                    humanObj.setDirection(1);

                state = 1;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                state = 0;
            }
        }

        return true;
    }

    public void updateTouchEvent() {

        if(humanObj.getState() != Human.HUMAN_BAT ) {
            if(state == 0) {
                humanObj.update(Human.HUMAN_IDLE);
            } else if (state == 1) {
                humanObj.update(Human.HUMAN_RUN);
            }
        }
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

    }

    private void prepareViewCompute(int width, int height) {

        viewCompute.setPlantSand(BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.plant_1));
        viewCompute.setColumnWidth(width / 1.3f);
        viewCompute.setMarginWidth(Until.dp2px(getContext().getResources().getDisplayMetrics().density, 50));
        viewCompute.setRadius(Until.dp2px(getContext().getResources().getDisplayMetrics().density, 50));

        float[] widths = {mInterview.getWidth(), mAbout.getWidth(), mSkill.getWidth(), mExperience.getWidth()};
        float totalWidth = 0;

        for (float num : widths) {
            totalWidth += num;
        }

        viewCompute.setCurRect(new RectF(0, 0, totalWidth, height * 2));
        viewCompute.setPlantRect(new RectF(0, height - height / 8, totalWidth, height));
    }

    private void drawBackground(Canvas canvas) {

        canvas.drawColor(Until.Color_SKY);

    }

    private void computeRect() {

        RectF currentRect = viewCompute.getCurRect();

        float[] widths = {mInterview.getWidth(), mAbout.getWidth(), mSkill.getWidth(), mExperience.getWidth()};
        float[] heights = {mInterview.getHeight(), mAbout.getHeight(), mSkill.getHeight(), mExperience.getHeight()};

        float left, top, right = 0, bottom;

        for(int i = 0 ; i < widths.length ; i++) {

            left = i > 0 ? right : currentRect.left;
            top = currentRect.top;
            right = left + widths[i];
            bottom = top + heights[i];

            if (i == 0)
                mInterview.setDstRect(currentRect.left, top, right, bottom);
            else if (i == 1)
                mAbout.setDstRect(left, top, right, bottom);
            else if (i == 2)
                mSkill.setDstRect(left, top, right, bottom);
            else if(i == 3)
                mExperience.setDstRect(left, top, right, bottom);

        }
    }

    private void drawFPS(Canvas canvas) {

        long timeThisFrame = System.currentTimeMillis() - startFrameTime;

        if (timeThisFrame >= 1) {
            canvas.drawText(String.valueOf(1000 / timeThisFrame), 200, 100, textPaint);
        }

    }

    @Override
    public HolderBitmap getHolderBitmap() {
        return holderBitmap;
    }

    @Override
    public ViewCompute getViewCompute() {
        return viewCompute;
    }

    @Override
    public Human getHuman() {
        return humanObj;
    }
}
