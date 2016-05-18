package com.kuo.kuoresume.object;

import android.opengl.GLES20;

import com.kuo.kuoresume.data.SquareCoords;
import com.kuo.kuoresume.shaders.riGraphicTools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Created by Kuo on 2016/5/5.
 */
public class GLText {

    private static final int COORDS_PER_VERTEX = 3;
    private static final float TEXT_UV_BOX_WIDTH = 0.125f; // 64 / 512 = 0.125;
    private static final float RI_TEXT_SPACESIZE = 20f;
    private static final float TEXT_SPEACESIZE = 64f; // 512 / 8 = 64

    private int textSize = 64;

    private static final int SQUARE_SIZE = 12;
    private static final int UVS_SIZE = 8;

    public String text;
    public float x;
    public float y;
    public float[] color;

    private float scaling;

    public int width, height;

    private int mProgram;

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    private static int[] charSize = {36,29,30,34,25,25,34,33,
            11,20,31,24,48,35,39,29,
            42,31,27,31,34,35,46,35,
            31,27,30,26,28,26,31,28,
            28,28,29,29,14,24,30,18,
            26,14,14,14,25,28,31,32,
            0,38,39,12,36,34,0,0,
            0,38,0,0,0,0,0,0};

    private ArrayList<SquareCoords> squareCoordses = new ArrayList<>();
    private float[] totalCoords;
    private float[] totalUvss;
    private short[] totalDrawOrder;

    // 點的緩衝區
    private FloatBuffer vertexBuffer, uvBuffer, colorBuffer;

    // 索引值緩衝區
    private ShortBuffer drawListBuffer;

    public GLText(String txt, int textSize,float xcoord, float ycoord) {

        text = txt;
        x = xcoord;
        y = ycoord;
        color = new float[] {0f, 0f, 0f, 0f};

        this.textSize = textSize;

        scaling = textSize / TEXT_SPEACESIZE;

        computeCoords();
        setGLES();
    }

    public void draw(float[] mvpMartix) {

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation(mProgram, "s_texture");

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 1);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMartix, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, totalDrawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void setGLES() {

        try {
            ByteBuffer bb = ByteBuffer.allocateDirect(totalCoords.length * 4); // (# of coordinate values * 4 bytes per float)
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(totalCoords);
            vertexBuffer.position(0);

            ByteBuffer dlb = ByteBuffer.allocateDirect(totalDrawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
            drawListBuffer.put(totalDrawOrder);
            drawListBuffer.position(0);

            ByteBuffer bbr = ByteBuffer.allocateDirect(totalUvss.length * 4);
            bbr.order(ByteOrder.nativeOrder());
            uvBuffer = bbr.asFloatBuffer();
            uvBuffer.put(totalUvss);
            uvBuffer.position(0);

            int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                    riGraphicTools.vertexShaderCode);
            int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                    riGraphicTools.fragmentShaderCode);

            // create empty OpenGL ES Program
            mProgram = GLES20.glCreateProgram();

            // add the vertex shader to program
            GLES20.glAttachShader(mProgram, vertexShader);

            // add the fragment shader to program
            GLES20.glAttachShader(mProgram, fragmentShader);

            // creates OpenGL ES program executables
            GLES20.glLinkProgram(mProgram);
        } catch (Exception e) {}


    }

    private void computeCoords() {

        totalCoords = null;
        totalUvss = null;
        totalDrawOrder = null;

        int sumX = 0;

        for(int i = 0 ; i < text.length() ; i++) {

            int index = computeCharToIndex(text.charAt(i));

            if(index == -1)
                continue;

            int col = index % 8;
            int row = index / 8;

            SquareCoords mSquareCoords = new SquareCoords();

            float left = x + sumX;
            float top = y;
            float right = left + TEXT_SPEACESIZE * scaling;
            float bottom = top + TEXT_SPEACESIZE * scaling;

            sumX += charSize[index] * scaling;

            float[] squareCoords = new float[]{left, top, 0f,
                    left, bottom, 0f,
                    right, bottom, 0f,
                    right, top, 0f};

            left = col * TEXT_UV_BOX_WIDTH + 0.005f;
            top = row * TEXT_UV_BOX_WIDTH + 0.005f;
            right = left + TEXT_UV_BOX_WIDTH - 0.005f;
            bottom = top + TEXT_UV_BOX_WIDTH - 0.005f;

            float[] uvs = new float[]{left, top,
                    left, bottom,
                    right, bottom,
                    right, top};

            mSquareCoords.setDrawOrder((short) (totalCoords == null ? 0 : totalCoords.length / COORDS_PER_VERTEX));

            totalCoords = addAll(totalCoords, squareCoords);

            totalUvss = addAll(totalUvss, uvs);

            totalDrawOrder = addAll(totalDrawOrder, mSquareCoords.getDrawOrder());

        }

        width = sumX;
        height = (int) (TEXT_SPEACESIZE * scaling);
    }

    public void setLocation(float x, float y) {

        this.x = x;
        this.y = y;

        computeCoords();

        if(vertexBuffer != null)
            vertexBuffer.clear();

        if(drawListBuffer != null)
            drawListBuffer.clear();

        if(uvBuffer != null)
            uvBuffer.clear();

        try {
            ByteBuffer bb = ByteBuffer.allocateDirect(totalCoords.length * 4); // (# of coordinate values * 4 bytes per float)
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(totalCoords);
            vertexBuffer.position(0);

            ByteBuffer dlb = ByteBuffer.allocateDirect(totalDrawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
            drawListBuffer.put(totalDrawOrder);
            drawListBuffer.position(0);

            ByteBuffer bbr = ByteBuffer.allocateDirect(totalUvss.length * 4);
            bbr.order(ByteOrder.nativeOrder());
            uvBuffer = bbr.asFloatBuffer();
            uvBuffer.put(totalUvss);
            uvBuffer.position(0);
        } catch (Exception e) {}

    }

    private int computeCharToIndex(char c) {

        int ascii = (int) c;

        int index = -1;

        if(ascii > 64 && ascii < 91)
            index = ascii - 65;
        else if(ascii > 96 && ascii < 123)
            index = ascii - 97;
        else if(ascii > 47 && ascii < 58)
            index = ascii - 48 + 26;
        else if(ascii == 32)
            index = 47;

        return index;
    }


    public float[] addAll(float[] array1, float[] array2) {
        if (array1 == null)
            return clone(array2);
        else if (array2 == null)
            return clone(array1);

        float[] joinedArray = new float[array1.length + array2.length];

        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

        return joinedArray;
    }

    public float[] clone(float[] array) {

        if (array == null)
            return null;

        return array.clone();
    }

    public short[] addAll(short[] array1, short[] array2) {
        if (array1 == null)
            return clone(array2);
        else if (array2 == null)
            return clone(array1);

        short[] joinedArray = new short[array1.length + array2.length];

        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);

        return joinedArray;
    }

    public short[] clone(short[] array) {

        if (array == null)
            return null;

        return array.clone();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
