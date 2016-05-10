package com.kuo.kuoresume.script;

import android.content.Context;

import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.listener.ViewComputeListener;

/**
 * Created by Kuo on 2016/5/9.
 */
public class GLMessage extends ComputeRect {

    public GLMessage(Context context, ViewComputeListener viewComputeListener, ObjectListener objectListener) {
        super(context, viewComputeListener, objectListener);

        PLANT_RANGE_SIZE = 20;

        width = PLANT_RANGE_SIZE * (int) viewComputeListener.getViewCompute().getPlantSize();

        height = (int) viewComputeListener.getViewCompute().getContentRect().height();

    }

}
