package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kuo.kuoresume.R;
import com.kuo.kuoresume.animation.SpriteAnimation;
import com.kuo.kuoresume.compute.ViewCompute;
import com.kuo.kuoresume.listener.ObjectListener;
import com.kuo.kuoresume.object.TalkWindow;
import com.kuo.kuoresume.until.Until;

import java.util.ArrayList;

/**
 * Created by Kuo on 2016/4/20.
 */
public class Interview {

    private int[] plantArray = {1, 1, 1, 1, 1, 1, 1, 1};

    private int RANGE_SIZE;

    private int plantSize;

    private int width, height;

    private Bitmap interview, tree_0, tree_1;

    private RectF dstRect = new RectF(0, 0, 0, 0);

    private SpriteAnimation spriteAnimation;

    Paint testPaint;

    private TalkWindow talkWindow;

    private ObjectListener objectListener;

    public Interview(Context context, int width, int height, ObjectListener objectListener) {

        plantSize =  Until.dp2px(context.getResources().getDisplayMetrics().density, 50);

        RANGE_SIZE = width / plantSize + 1;

        this.objectListener = objectListener;
        this.width = RANGE_SIZE * plantSize;
        this.height = height;

        testPaint = new Paint();
        testPaint.setStyle(Paint.Style.STROKE);
        testPaint.setStrokeWidth(Until.dp2px(context.getResources().getDisplayMetrics().density, 10));
        testPaint.setColor(Color.BLUE);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        interview = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.interview),
                width / 2, width / 4, true);

        tree_0 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.tree_0, options);
        tree_1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.tree_1, options);

        spriteAnimation = new SpriteAnimation(100, new Rect(0, 0, interview.getWidth(), interview.getHeight()), interview.getWidth(), interview.getHeight(), 1);
        spriteAnimation.setOnUpdateListener(interviewUpdateListener);

        talkWindow = new TalkWindow(context,
                "The man wants a job,\n but this is not my business.\n" +
                        "By the way, I'm really ugly. \n",
                Until.dp2px(context.getResources().getDisplayMetrics().density, 20));

        computePlant();
    }

    public void setDstRect(float left, float top, float right, float bottom) {
        dstRect.set(left, top, right, bottom);
    }

    public RectF getDstRect() {
        return dstRect;
    }

    public int getWidth() {
        return width;
    }


    public void draw(Canvas canvas) {

        Human human = objectListener.getHuman();

        talkWindow.setDstRect(dstRect.centerX() - talkWindow.getWidth() / 2,
                human.dstRect.top - talkWindow.getHeight() * talkWindow.getCount(),
                dstRect.centerX() + talkWindow.getWidth() / 2,
                human.dstRect.top);

        drawPlant(canvas, objectListener.getHolderBitmap().plantSand, objectListener.getViewCompute());
        canvas.drawRect(dstRect, testPaint);
        spriteAnimation.start();
        drawTree(canvas);
        //canvas.drawBitmap(interview, interviewRect.left, interviewRect.top, null);
        talkWindow.draw(canvas);

    }

    int[] treeArray = {0, 0, 1, 1, 0};

    private void drawTree(Canvas canvas) {

        float top = height - plantSize - tree_0.getHeight() + 10;
        float margin = 30;

        for(int i = 0 ; i < treeArray.length ; i++) {

            float left = dstRect.left + dstRect.width() / 4 + (tree_0.getWidth() / 2) * i;

            if(treeArray[i] == 0)
                canvas.drawBitmap(tree_0, left, top, null);
            else
                canvas.drawBitmap(tree_1, left, top, null);

        }

    }

    ArrayList<RectF> plantRects = new ArrayList<>();

    private void computePlant() {

        for (int i = 0; i < RANGE_SIZE; i++) {

            float left = dstRect.left + plantSize * i;
            float top = height - plantSize;
            float right = left + plantSize;
            float bottom = height;

            RectF dstRect = new RectF(left, top, right, bottom);
            plantRects.add(dstRect);
        }
    }

    private void drawPlant(Canvas canvas, Bitmap bitmap, ViewCompute viewCompute) {

        RectF contentRect = viewCompute.getContentRect();

        Rect srcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        int count = 0;
        for(RectF rectF : plantRects) {

            float left = dstRect.left + plantSize * count;
            float top = height - plantSize;
            float right = left + plantSize;
            float bottom = height;

            rectF.set(left, top, right, bottom);

            if(contentRect.contains(rectF.left, rectF.top)
                    || contentRect.contains(rectF.right  - 1, rectF.bottom - 1))
                canvas.drawBitmap(bitmap, srcRect, rectF, null);
            count++;
        }
    }

    private RectF interviewRect = new RectF(0, 0, 0, 0);
    private int direction = 1;
    private int speed = 5;

    private SpriteAnimation.OnUpdateListener interviewUpdateListener = new SpriteAnimation.OnUpdateListener() {
        @Override
        public void onUpdate(int currentHorizontalFrame, int currentVerticalFrame) {

            if(direction == 1 && interviewRect.centerY() < dstRect.centerY() / 2 - interview.getHeight() / 2) {
                interviewRect.left = dstRect.centerX() - interview.getWidth() / 2;
                interviewRect.top += speed * direction;

                if(interviewRect.top >= dstRect.centerY() / 2 - interview.getHeight() / 2)
                    interviewRect.top = dstRect.centerY() / 2 - interview.getHeight() / 2;
            }

        }

        @Override
        public void end() {

        }
    };

    public int getHeight() {
        return height;
    }
}
