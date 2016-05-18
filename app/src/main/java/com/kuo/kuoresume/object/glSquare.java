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
public class GLSquare {

    // Use to access and set the view transformation
    private int mMVPMatrixHandle;

    /* SHADER Solid
     *
     * This shader is for rendering a colored primitive.
     *
     */
    public static final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    public static String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    static final int COORDS_PER_VERTEX = 3;

    private final int mProgram;

    private float squareCoords[] = { 10.0f, 200f, 0.0f,  // left top
            10.0f, 100f, 0.0f,   //  left bottom
            100f, 100f, 0.0f,   //  right bottom
            100f, 200f, 0.0f }; //  right top

    // 連接點的次序
    private short[] drawOrder = { 0, 1, 2, 0, 2, 3 };

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    // 點的緩衝區
    private FloatBuffer vertexBuffer;

    // 索引值緩衝區
    private ShortBuffer drawListBuffer;

    private RectF dstRect = new RectF(0, 0, 0, 0);
    private RectF srcRect = new RectF(0, 0, 0, 0);

    public GLSquare(RectF srcRect, float[] color) {

        this.srcRect = srcRect;

        setDstRect(srcRect.left, srcRect.top, srcRect.right, srcRect.bottom);

        this.color = color;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4); // (# of coordinate values * 4 bytes per float)
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2); // (# of coordinate values * 2 bytes per short)
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] mvpMatrix) {

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
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

    public RectF getSrcRect() {
        return srcRect;
    }

    public RectF getDstRect() {
        return dstRect;
    }
}
