package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;
import com.kuo.kuoresume.object.GLSquare;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLInterview extends ComputeRect{

    private float INTERVIEW_IMAGE_WIDTH = 800 * 0.7f;
    private float INTERVIEW_IMAGE_HEIGHT = 600 * 0.7f;

    private GLImage interviewImage, officeLight;

    private GLSquare ground;

    public GLInterview(Context context, ViewComputeListener viewComputeListener) {
        super(context, viewComputeListener);

        INTERVIEW_IMAGE_WIDTH = INTERVIEW_IMAGE_WIDTH * viewComputeListener.getScaling();
        INTERVIEW_IMAGE_HEIGHT = INTERVIEW_IMAGE_HEIGHT * viewComputeListener.getScaling();

        PLANT_RANGE_SIZE =18;

        width = viewComputeListener.getViewCompute().getPlantSize() * PLANT_RANGE_SIZE;

        height = viewComputeListener.getViewCompute().getContentRect().height();

        officeLight = new GLImage(new RectF(0, height - viewComputeListener.getViewCompute().getPlantSize() - INTERVIEW_IMAGE_HEIGHT,
                INTERVIEW_IMAGE_WIDTH, height - viewComputeListener.getViewCompute().getPlantSize()));

        interviewImage = new GLImage(new RectF(width / 2 - INTERVIEW_IMAGE_WIDTH / 2,
                height - viewComputeListener.getViewCompute().getPlantSize() - INTERVIEW_IMAGE_HEIGHT,
                width / 2 + INTERVIEW_IMAGE_WIDTH / 2, height - viewComputeListener.getViewCompute().getPlantSize()));

        ground = new GLSquare(new RectF(0, height - viewComputeListener.getViewCompute().getPlantSize(), width, height),
                new float[] {0, 0, 0, 1});

        computeRect();
    }

    public void draw(float[] m) {
        ground.draw(m);
        interviewImage.draw(m, 13);
        officeLight.draw(m, 14);
    }

    public void computeRect() {
        ground.computeDstRect(dstRect);
        officeLight.computeDstRect(dstRect);
        interviewImage.computeDstRect(dstRect);
    }



}
