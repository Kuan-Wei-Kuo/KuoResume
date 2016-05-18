package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.until.Until;

import java.util.Random;

/**
 * Created by Kuo on 2016/5/12.
 */
public class LoadingScreen extends View {

    protected long lastFrameChangeTime = 0;
    protected long frameLengthInMilliseconds = 100;

    protected long textLastFrameChangeTime = 0;
    protected long textFrameLengthInMilliseconds = 800;

    private Bitmap loadingBitmap;

    private RectF srcRect;

    private Matrix matrix;

    private int angles = 0;

    private Paint paint;

    private Random random;

    private String[] loadingTexts = {"Why don't you order a sandwich ?",
            "Follow the white rabbit",
            "We love you just the way you are",
            "You're not in Kansas any more",
            "We're testing your patience",
            "Loading humorous message"};

    private String loadingText;

    public LoadingScreen(Context context) {
        super(context);

        createLoadingBitmap();
    }

    public LoadingScreen(Context context, AttributeSet attrs) {
        super(context, attrs);

        createLoadingBitmap();
    }

    public LoadingScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        createLoadingBitmap();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long time  = System.currentTimeMillis();

        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {

            lastFrameChangeTime = time;

            angles += 20;

            if (angles >= 360)
                angles = 0;

            matrix.reset();
            matrix.postRotate(angles, srcRect.centerX(), srcRect.centerY());
            matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        }

        if(time > textLastFrameChangeTime + textFrameLengthInMilliseconds) {

            textLastFrameChangeTime = time;

            loadingText = loadingTexts[random.nextInt(loadingTexts.length)];
        }

        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(loadingBitmap, matrix, null);
        canvas.drawText(loadingText, getWidth() / 2, getHeight() / 1.5f , paint);

        postInvalidate();
    }

    private void createLoadingBitmap() {

        loadingBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.loading_image);

        srcRect = new RectF(0, 0, loadingBitmap.getWidth(), loadingBitmap.getHeight());

        matrix = new Matrix();

        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(Until.dp2px(getContext() .getResources().getDisplayMetrics().density, 20));

        random = new Random();

        loadingText = loadingTexts[random.nextInt(loadingTexts.length)];
    }

}
