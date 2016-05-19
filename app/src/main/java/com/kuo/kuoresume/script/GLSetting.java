package com.kuo.kuoresume.script;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.kuo.kuoresume.R;

/**
 * Created by Kuo on 2016/5/6.
 */
public class GLSetting {

    private int[] textureNames;


    public GLSetting(Context context, int textureSize) {

        textureNames = new int[31];
        //GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, textureNames, 0);
        GLES20.glGenTextures(textureSize, textureNames, 0);

        GLES20.glClearColor(0.7f, 0.7f, 0.9f, 1.0f);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Set GL_BLEND And GL_ONE_MINUS_SRC_ALPHA
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        createBitmap(context);
    }

    private void createBitmap(Context context) {

        int[] resId = {R.mipmap.stickman_run, R.mipmap.font, R.mipmap.plant_1, R.mipmap.tag, R.mipmap.stickman_jump,
                R.mipmap.bg_1, R.mipmap.office_door, R.mipmap.tag_2, R.mipmap.build_85, R.mipmap.sea, R.mipmap.office,
                R.mipmap.cloud, R.mipmap.office_computer, R.mipmap.tree_1, R.mipmap.tree_2, R.mipmap.tree_3, R.mipmap.buddha,
                R.mipmap.circle_orange, R.mipmap.my_chart_lib_icon, R.mipmap.firstaid, R.mipmap.basketball_board_icon,
                R.mipmap.urcoco_icon, R.mipmap.logdown_main, R.mipmap.logo_gmail_128px, R.mipmap.share_icon,
                R.mipmap.github_logo, R.mipmap.stickman_idle, R.mipmap.own_music, R.mipmap.computer_screen, R.mipmap.flicker_light, R.mipmap.gold_box};

        for(int i = 0 ; i < resId.length ; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId[i]);
            addTexture(i, bitmap);
            bitmap.recycle();
        }

    }

    private void addTexture(int index, Bitmap bitmap) {

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[index]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }
}
