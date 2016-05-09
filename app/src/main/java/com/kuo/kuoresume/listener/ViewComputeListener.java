package com.kuo.kuoresume.listener;

import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.script.GLCharacter;

/**
 * Created by Kuo on 2016/5/4.
 */
public interface ViewComputeListener {
    ViewCompute getViewCompute();

    GLCharacter getGLCharacter();
}
