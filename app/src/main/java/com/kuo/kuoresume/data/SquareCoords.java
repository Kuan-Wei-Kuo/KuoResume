package com.kuo.kuoresume.data;

/**
 * Created by Kuo on 2016/5/6.
 */
public class SquareCoords {

    int index;
    float[] coords;
    float[] uvs;

    private short[] drawOrder = { 0, 1, 2, 0, 2, 3 };

    public void setCoords(float left, float top, float right, float bottom) {
        coords = new float[]{left, top, 0f,
                left, bottom, 0f,
                right, bottom, 0f,
                right, top, 0f};
    }

    public void setUvs(float left, float top, float right, float bottom) {
        uvs = new float[]{left, top,
                left, bottom,
                right, bottom,
                right, top};
    }

    public void setDrawOrder(short base) {
        for(int i = 0 ; i < drawOrder.length ; i++) {
            drawOrder[i] = (short) (drawOrder[i] + base);
        }
    }

    public float[] getCoords() {
        return coords;
    }

    public float[] getUvs() {
        return uvs;
    }

    public short[] getDrawOrder() {
        return drawOrder;
    }
}
