package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.GLImage;

/**
 * Created by Kuo on 2016/5/19.
 */
public class GLBackground extends ComputeRect {

    private float BACKGROUND_WIDTH = 800;
    private float BACKGROUND_HEIGHT = 600;
    private float OFFICE_WIDTH = 800;
    private float OFFICE_HEIGHT = 600;

    private GLImage bg_1, officeComputer, officeDoor;

    public GLBackground(Context context, ViewComputeListener viewComputeListener) {
        super(context, viewComputeListener);

        BACKGROUND_WIDTH = BACKGROUND_WIDTH * viewComputeListener.getScaling();
        BACKGROUND_HEIGHT = BACKGROUND_HEIGHT * viewComputeListener.getScaling();

        OFFICE_WIDTH = OFFICE_WIDTH * viewComputeListener.getScaling();
        OFFICE_HEIGHT = OFFICE_HEIGHT * viewComputeListener.getScaling();


        officeComputer = new GLImage(new RectF(0, 0, BACKGROUND_WIDTH, OFFICE_HEIGHT));
        officeComputer.computeDstRect(dstRect);

        officeDoor = new GLImage(new RectF(OFFICE_WIDTH + BACKGROUND_WIDTH * 0.5f, 0, OFFICE_WIDTH + OFFICE_WIDTH + BACKGROUND_WIDTH * 0.5f, OFFICE_HEIGHT));
        officeDoor.computeDstRect(dstRect);

        bg_1 = new GLImage(new RectF(0, 0, OFFICE_WIDTH + OFFICE_WIDTH + BACKGROUND_WIDTH * 0.5f, BACKGROUND_HEIGHT));
        bg_1.computeDstRect(dstRect);


        width = OFFICE_WIDTH + OFFICE_WIDTH + BACKGROUND_WIDTH * 0.5f;
        height = BACKGROUND_HEIGHT;
    }

    public void computeRect() {
        bg_1.computeDstRect(dstRect);
        officeComputer.computeDstRect(dstRect);
        officeDoor.computeDstRect(dstRect);
    }

    public void draw(float[] m) {
        //bg_1.draw(m, 5);
        officeComputer.draw(m, 12);
        officeDoor.draw(m, 6);
    }
}
