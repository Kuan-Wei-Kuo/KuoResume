package com.kuo.kuoresume.compute;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Created by Kuo on 2016/4/17.
 */
public class ViewCompute {

    private RectF curRect, contentRect, plantRect;

    private float columnWidth, marginWidth, radius;

    private Bitmap plantSand;

    private float plantSize;

    public void setPlantSize(float plantSize) {
        this.plantSize = plantSize;
    }

    public float getPlantSize() {
        return plantSize;
    }

    public void setPlantSand(Bitmap plantSand) {
        this.plantSand = plantSand;
    }

    public Bitmap getPlantSand() {
        return plantSand;
    }

    public void setContentRect(RectF contentRect) {
        this.contentRect = contentRect;
    }

    public void setCurRect(RectF curRect) {
        this.curRect = curRect;
    }

    public void setPlantRect(RectF plantRect) {
        this.plantRect = plantRect;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
    }

    public void setMarginWidth(float marginWidth) {
        this.marginWidth = marginWidth;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public RectF getContentRect() {
        return contentRect;
    }

    public RectF getCurRect() {
        return curRect;
    }

    public RectF getPlantRect() {
        return plantRect;
    }

    public float getColumnWidth() {
        return columnWidth;
    }

    public float getMarginWidth() {
        return marginWidth;
    }

    public float getRadius() {
        return radius;
    }
}
