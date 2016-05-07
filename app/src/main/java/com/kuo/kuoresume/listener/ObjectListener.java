package com.kuo.kuoresume.listener;

import com.kuo.kuoresume.compute.HolderBitmap;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.script.Human;

/**
 * Created by Kuo on 2016/4/30.
 */
public interface ObjectListener {

    HolderBitmap getHolderBitmap();

    ViewCompute getViewCompute();

    Human getHuman();

}
