package com.kuo.kuoresume.script;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by Kuo on 2016/5/6.
 */
public class GLSetting {

    int[] textureNames;

    public GLSetting(int textureSize) {

        textureNames = new int[textureSize];
        //GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, textureNames, 0);
        GLES20.glGenTextures(textureSize, textureNames, 0);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Set GL_BLEND And GL_ONE_MINUS_SRC_ALPHA
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

    }

    public void addTexture(int index, Bitmap bitmap) {

        Log.d("index", index + "");

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + index);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[index]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }
}
