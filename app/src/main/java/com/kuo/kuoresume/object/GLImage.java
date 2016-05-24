package com.kuo.kuoresume.object;

import android.graphics.RectF;
import android.opengl.GLES20;

import com.kuo.kuoresume.shaders.riGraphicTools;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Kuo on 2016/5/4.
 */
public class GLImage {

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    static final int COORDS_PER_VERTEX = 3;

    private final int mProgram;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private final int vertexCount;

    private float[] squareCoords;

    // Create our UV coordinates.
    private float[] uvs = new float[] {
            0.0f, 0.0f, //left top
            0.0f, 1.0f, //left bottom
            1.0f, 1.0f, //right bottom
            1.0f, 0.0f, //right top
    };
    // 連接點的次序
    private short[] drawOrder = { 0, 1, 2, 0, 2, 3 };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    // 點的緩衝區
    private FloatBuffer vertexBuffer, uvBuffer;

    // 索引值緩衝區
    private ShortBuffer drawListBuffer;

    private RectF dstRect = new RectF(0, 0, 0, 0);

    private RectF srcRect = new RectF(0, 0, 0, 0);

    public GLImage(RectF srcRect) {

        this.srcRect = srcRect;

        setDstRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);

        setUVS(uvs);

        vertexCount = squareCoords.length / COORDS_PER_VERTEX;

        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

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
    }

    public void draw(float[] mvpMatrix, int textureIndex) {

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(mProgram, "a_texCoord");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (mProgram, "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i (mSamplerLoc, textureIndex);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    public void setSrcRect(RectF srcRect) {
        this.srcRect = srcRect;
    }

    public void setDstRect(float left, float top, float right, float bottom) {

        dstRect.set(left, top, right, bottom);

        squareCoords = new float[] {
                dstRect.left, dstRect.top, 0.0f,
                dstRect.left, dstRect.bottom, 0.0f,
                dstRect.right, dstRect.bottom, 0.0f,
                dstRect.right, dstRect.top, 0.0f};

        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

    }

    public void computeDstRect(RectF rawDstRect) {

        float left = rawDstRect.left + srcRect.left;
        float top = rawDstRect.top + srcRect.top;
        float right = left + srcRect.width();
        float bottom = top + srcRect.height();

        setDstRect(left, top, right, bottom);
    }

    public void setUVS(float [] uvs) {
        ByteBuffer bbr = ByteBuffer.allocateDirect(uvs.length * 4);
        bbr.order(ByteOrder.nativeOrder());
        uvBuffer = bbr.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    public RectF getSrcRect() {
        return srcRect;
    }

    public RectF getDstRect() {
        return dstRect;
    }
}
