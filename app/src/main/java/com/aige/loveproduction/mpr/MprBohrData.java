package com.aige.loveproduction.mpr;

import com.aige.loveproduction.enums.MprPosition;

/**
 * @author 张文科
 * 储存钉子的数据，以及对一些坐标的处理
 */
public class MprBohrData implements MprData{
    private final MprMaster master;
    private final StringBuilder coordText = new StringBuilder();
    private float finalX;
    private float finalY;
    private float XA;
    private float YA;
    private float DU;
    private float TI;
    private MprPosition position = MprPosition.NULL;
    private MprPosition position2 = MprPosition.NULL;
    public MprBohrData(MprMaster master) {
        this.master = master;
    }

    @Override
    public void initData(){
        parsePosition();
        parseXYPosition();
        setFinalData();
    }
    /**
     * 获取当前坐标在图形中的位置
     * 左下、左上、右上、右下
     */
    public void parsePosition(){
        position = master.getXYPosition(XA,YA);
    }
    /**
     * 获取当前在图形边界上的点的位置
     * 左、右、上、下
     * */
    private void parseXYPosition(){
        position2 = master.getPosition(XA,YA);
    }
    private void setFinalData(){
        String x = finalX % 1.0 == 0 ? String.valueOf((int)finalX): String.valueOf(finalX);
        String y = finalY % 1.0 == 0 ? String.valueOf((int)finalY): String.valueOf(finalY);
        coordText.append("x=").append(x).append(",")
                .append("y=").append(y);
    }
    public String getCoordText(){
        return coordText.toString();
    }
    /**
     * 获取该坐标的位置
     */
    @Override
    public MprPosition getPosition() {
        return position;
    }

    public MprPosition getPosition2() {
        return position2;
    }

    public void setPosition2(MprPosition position2) {
        this.position2 = position2;
    }

    public float getXA() {
        return XA;
    }

    public void setXA(float XA) {
        this.XA = XA;
        setFinalX(XA);
    }

    public float getYA() {
        return YA;
    }

    public void setYA(float YA) {
        this.YA = YA;
        setFinalY(YA);
    }

    public float getDU() {
        return DU;
    }

    public void setDU(float DU) {
        this.DU = DU;
    }

    public float getTI() {
        return TI;
    }

    public void setTI(float TI) {
        this.TI = TI;
    }

    public float getFinalX() {
        return finalX;
    }

    public void setFinalX(float finalX) {
        this.finalX = finalX;
    }

    public float getFinalY() {
        return finalY;
    }

    public void setFinalY(float finalY) {
        this.finalY = finalY;
    }

    @Override
    public void setScale(float scale) {
        this.XA *= scale;
        this.YA *= scale;
        this.DU *= scale;
        this.TI *= scale;

    }
    @Override
    public MprBohrData cloneData(MprMaster master){
        MprBohrData bohrModul = new MprBohrData(master);
        bohrModul.setXA(this.XA);
        bohrModul.setYA(this.YA);
        bohrModul.setDU(this.DU);
        bohrModul.setTI(this.TI);
        bohrModul.initData();
        return bohrModul;
    }

    @Override
    public String toString() {
        return "MprBohrModul{" +
                "XA=" + XA +
                ", YA=" + YA +
                ", DU=" + DU +
                ", TI=" + TI +
                '}';
    }
}
