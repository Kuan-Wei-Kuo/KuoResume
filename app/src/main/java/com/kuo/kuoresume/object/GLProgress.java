package com.kuo.kuoresume.object;

import android.content.Context;
import android.graphics.RectF;
import android.util.Log;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;
import com.kuo.kuoresume.script.ComputeRect;

/**
 * Created by Kuo on 2016/5/12.
 */
public class GLProgress extends ComputeRect{

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    private float[] mRotationMatrix = new float[16];

    private Image image;

    private GLImageText glText;

    private String[] loadingText = {"why don't you order a sandwich",
            "follow the white rabbit",
            "we love you just the way you are",
            "you're not in Kansas any more",
            "we're testing your patience",
            "Loading humorous message"};

    public GLProgress(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        image = new Image(new RectF(0, 0, 150, 150));
        glText = new GLImageText(context, "we love you just the way you are", 0, 0);

        width = (int) viewComputeListener.getViewCompute().getContentRect().width();
        height = (int) viewComputeListener.getViewCompute().getContentRect().height();
    }


    public void draw(float[] m) {

        long time  = System.currentTimeMillis();

        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;

            //float angle = 0.090f * ((int) time);
            //Matrix.setRotateM(mRotationMatrix, 0, 30, 0, 0, -1.0f);
            //Matrix.multiplyMM(m, 0, mRotationMatrix, 0, m, 0);

        }
        image.draw(m, 5);
        glText.draw(m);
    }

    public void computeRect() {

        RectF srcRect = image.getSrcRect();

        float left = srcRect.left;
        float top = srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        Log.d("left", left + "");

        image.setDstRect(left, top, right, bottom);
    }

}
