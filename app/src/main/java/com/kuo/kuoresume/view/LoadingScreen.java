package com.kuo.kuoresume.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

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

    private boolean isFirstDraw = true, isRun = true;

    private String[] loadingTexts = {"你怎麼不去點的三明治?",
            "正在加快載入中...",
            "玩家等到不開心囉!",
            "不小心切斷電源了...",
            "將重新啟動...",
            "這是一則載入的訊息..."};

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

        if(isRun) {

            long time  = System.currentTimeMillis();

            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {

                lastFrameChangeTime = time;

                angles += 30;

                if (angles >= 360)
                    angles = 0;

                matrix.reset();
                matrix.postRotate(angles, srcRect.centerX(), srcRect.centerY());
                matrix.postTranslate(getWidth() / 2 - srcRect.width() / 2, getHeight() / 2 - srcRect.height() / 2);
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

    public boolean isFirstDraw() {
        return isFirstDraw;
    }

    public void setFirstDraw(boolean firstDraw) {
        isFirstDraw = firstDraw;
    }

    public void setRun(boolean run) {
        isRun = run;
    }
}
