package com.kuo.kuoresume.listener;

import com.kuo.kuoresume.presents.ViewCompute;
import com.kuo.kuoresume.script.GLCharacter;

/**
 * Created by Kuo on 2016/5/4.
 */
public interface ViewComputeListener {

    float getScaling();

    void computeRect();

    ViewCompute getViewCompute();

    GLCharacter getGLCharacter();
}
