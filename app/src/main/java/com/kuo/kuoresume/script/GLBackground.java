package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.RectF;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.object.Image;

/**
 * Created by Kuo on 2016/5/19.
 */
public class GLBackground extends ComputeRect {

    private float BACKGROUND_WIDTH = 800;
    private float BACKGROUND_HEIGHT = 600;
    private float OFFICE_COMPUTER_WIDTH = 800;
    private float OFFICE_COMPUTER_HEIGHT = 600;


    private Image bg_1, officeComputer;

    public GLBackground(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        BACKGROUND_WIDTH = BACKGROUND_WIDTH * viewComputeListener.getScaling();
        BACKGROUND_HEIGHT = BACKGROUND_HEIGHT * viewComputeListener.getScaling();

        OFFICE_COMPUTER_WIDTH = OFFICE_COMPUTER_WIDTH * viewComputeListener.getScaling();
        OFFICE_COMPUTER_HEIGHT = OFFICE_COMPUTER_HEIGHT * viewComputeListener.getScaling();


        officeComputer = new Image(new RectF(0, 0, OFFICE_COMPUTER_WIDTH, OFFICE_COMPUTER_HEIGHT));
        officeComputer.computeDstRect(dstRect);

        bg_1 = new Image(new RectF(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT));
        bg_1.computeDstRect(dstRect);
    }

    public void computeRect() {
        bg_1.computeDstRect(dstRect);
        officeComputer.computeDstRect(dstRect);
    }

    public void draw(float[] m) {
        bg_1.draw(m, 5);
        officeComputer.draw(m, 12);
    }
}
