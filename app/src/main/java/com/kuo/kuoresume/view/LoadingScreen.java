package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Kuo on 2016/5/12.
 */
public class LoadingScreen extends View {

    public LoadingScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoadingScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingScreen(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        
    }
}
