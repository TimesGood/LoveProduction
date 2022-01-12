package com.aige.loveproduction.mpr;

/**
 * @author 张文科
 * 额外增加的数据
 */
public class MprExtraData implements MprData{
    private float textSize = 16f;//字体大小
    private float offsetX = 50f;//字体X轴偏移量
    private float offsetY = 12f;//字体Y轴偏移量
    private float strokeWidth = 1f;//粗细
    private float masterDistance = 100f;//距离线与主图形相距距离
    private float textDistance = 20f;//距离线中间缺口大小

    public float getTextSize() {
        return this.textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getOffsetX() {
        return this.offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return this.offsetY;
    }

    public void setOffsetY(float offsetY1) {
        this.offsetY = offsetY1;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public float getMasterDistance() {
        return this.masterDistance;
    }

    public void setMasterDistance(float masterLine) {
        this.masterDistance = masterLine;
    }

    public float getTextDistance() {
        return textDistance;
    }

    public void setTextDistance(float edge) {
        this.textDistance = edge;
    }

    @Override
    public void setScale(float scale) {
        this.textSize *= scale;
        this.offsetX *= scale;
        this.offsetY *= scale;
        this.strokeWidth *= scale;
        this.strokeWidth = this.strokeWidth >= 3f ? 3f : Math.max(this.strokeWidth, 1f);
        this.masterDistance *= scale;
        this.textDistance *= scale;
    }

    @Override
    public MprData cloneData(MprMaster master) {
        MprExtraData extraModul = new MprExtraData();
        extraModul.setMasterDistance(this.masterDistance);
        extraModul.setOffsetX(this.offsetX);
        extraModul.setOffsetY(this.offsetY);
        extraModul.setTextSize(this.textSize);
        extraModul.setStrokeWidth(this.strokeWidth);
        extraModul.setTextDistance(this.textDistance);
        return extraModul;
    }

    @Override
    public void initData() {

    }
}
