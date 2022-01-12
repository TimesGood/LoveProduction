package com.aige.loveproduction.mpr;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张文科
 * Mpr图形数据集合
 */
public class MprDataWrap {
    private MprMaster master;
    private List<MprData> bohrHoriz;
    private List<MprData> bohrVert;
    private List<MprData> bohrVertCross;
    private List<MprData> cutting;
    private MprExtraData extraModul = new MprExtraData();
    private final float[] xy = new float[2];


    /**
     * 获取两点之间中点的坐标
     * @param startX 点1X坐标
     * @param startY 点1Y坐标
     * @param stopX 点2X坐标
     * @param stopY 点2Y坐标
     * @return 0：x，1：y
     */
    public float[] getXYMiddle(float startX,float startY,float stopX,float stopY) {
        xy[0] = Math.abs((startX+stopX)/2);
        xy[1] = Math.abs((startY+stopY)/2);
        return xy;

    }
    /**
     * 获取两点之间的距离
     * @param startX 点1X坐标
     * @param startY 点1Y坐标
     * @param stopX 点2X坐标
     * @param stopY 点2Y坐标
     */
    public float getDistance(float startX,float startY,float stopX,float stopY) {
        return (float) Math.sqrt(Math.pow(startX-stopX,2)+Math.pow(startY-stopY,2));
    }
    /**
     * 初始化数据，在get、set之后需要主动调用
     */
    public void initData(){
        if(bohrHoriz != null){
            bohrHoriz.forEach(MprData::initData);
        }
        if(bohrVert != null){
            bohrVert.forEach(MprData::initData);
        }
        if(bohrVertCross != null){
            bohrVertCross.forEach(MprData::initData);
        }
        if(cutting != null){
            cutting.forEach(MprData::initData);
        }
    }

    /**
     * 获取集合中各个位置的数据垂直十字钉数据
     */
    public MprBohrData getMprBohrVertCrossData(int position){
        if(getBohrVertCrossSize() == 0) return null;
        return (MprBohrData) this.bohrVertCross.get(position);
    }
    /**
     * 获取集合中各个位置的数据垂直钉子数据
     */
    public MprBohrData getMprBohrVertData(int position){
        if(getBohrVertSize() == 0) return null;
        return (MprBohrData) this.bohrVert.get(position);
    }

    /**
     * 获取集合中某个位置的数据水平钉子数据
     */
    public MprBohrData getMprBohrHorizData(int position){
        if(getBohrHorizSize() == 0) return null;
        return (MprBohrData) this.bohrHoriz.get(position);
    }
    public MprCuttingData getMprCuttingData(int position) {
        if(getCuttingSize() == 0) return null;
        return (MprCuttingData) this.cutting.get(position);
    }
    /**获取切割曲线*/
    public MprCuttingData getCuttingCurve(){
        if(getCuttingSize() == 0) return null;
        for(byte i = 0;i < this.cutting.size();i++) {
            MprCuttingData mprData = (MprCuttingData) this.cutting.get(i);
            if(mprData.getMap().size()/2 == 2) {
                return mprData;
            }
        }
        return null;
    }
    /**获取切割环绕线*/
    public MprCuttingData getCuttingEncircle(){
        if(getCuttingSize() == 0) return null;
        return ((MprCuttingData)this.cutting.get(getCuttingSize()-1));
    }



    /**全体缩放*/
    public void scale(float scale) {
        this.master.setScale(scale);
        this.bohrHoriz.forEach(v -> {
            v.setScale(scale);
        });
        this.bohrVert.forEach(v -> {
            v.setScale(scale);
        });
        this.bohrVertCross.forEach(v -> {
            v.setScale(scale);
        });
        this.cutting.forEach(v -> {
            v.setScale(scale);
        });
        this.extraModul.setScale(scale);
    }
    /**克隆一个全新的数据*/
    public MprDataWrap cloneData(){
        MprDataWrap clone = new MprDataWrap();
        MprMaster master = (MprMaster) this.master.cloneData(null);
        List<MprData> bohrHoriz = new ArrayList<>();
        this.bohrHoriz.forEach(v -> {
            bohrHoriz.add(v.cloneData(master));
        });
        List<MprData> bohrVert = new ArrayList<>();
        this.bohrVert.forEach(v -> {
            bohrVert.add(v.cloneData(master));
        });
        List<MprData> bohrVertCross = new ArrayList<>();
        this.bohrVertCross.forEach(v -> {
            bohrVertCross.add(v.cloneData(master));
        });
        List<MprData> cutting = new ArrayList<>();
        this.cutting.forEach(v -> {
            cutting.add(v.cloneData(master));
        });
        MprExtraData extraModul = (MprExtraData)this.extraModul.cloneData(null);
        clone.setMaster(master);
        clone.setBohrHoriz(bohrHoriz);
        clone.setBohrVert(bohrVert);
        clone.setBohrVertCross(bohrVertCross);
        clone.setCutting(cutting);
        clone.setExtraModul(extraModul);
        return clone;
    }
    //********************************各数据数量*************************************
    /**获取切割线数量*/
    public int getCuttingSize(){
        return cutting == null ? 0:cutting.size();
    }
    /**获取垂直十字钉数量*/
    public int getBohrVertCrossSize(){
        return bohrVertCross == null ? 0:bohrVertCross.size();
    }
    /**获取垂直钉子数量*/
    public int getBohrVertSize(){
        return bohrVert == null ? 0:bohrVert.size();
    }
    /**获取水平钉子数量*/
    public int getBohrHorizSize(){
        return bohrHoriz == null?0:bohrHoriz.size();
    }
    //*****************************************get、set*************************************************
    public MprMaster getMaster() {
        return this.master;
    }

    public void setMaster(MprMaster master) {
        this.master = master;
    }

    public List<MprData> getBohrHoriz() {
        return bohrHoriz;
    }

    public void setBohrHoriz(List<MprData> bohrHoriz) {
//        bohrHoriz.forEach(v -> v.parsePosition());
        this.bohrHoriz = bohrHoriz;
    }

    public List<MprData> getBohrVert() {
        return bohrVert;
    }

    public void setBohrVert(List<MprData> bohrVert) {
//        bohrVert.forEach(v -> v.parsePosition());
        this.bohrVert = bohrVert;
    }

    public List<MprData> getBohrVertCross() {
        return bohrVertCross;
    }

    public void setBohrVertCross(List<MprData> bohrVertCross) {
//        bohrVertCross.forEach(v -> v.parsePosition());
        this.bohrVertCross = bohrVertCross;
    }

    public List<MprData> getCutting() {
        return cutting;
    }

    public void setCutting(List<MprData> cutting) {
        this.cutting = cutting;
    }

    public MprExtraData getExtraModul() {
        return extraModul;
    }

    public void setExtraModul(MprExtraData extraModul) {
        this.extraModul = extraModul;
    }

    @Override
    public String toString() {
        return "MprDataWrap{" +
                "master=" + master +
                ", bohrHoriz=" + bohrHoriz +
                ", bohrVert=" + bohrVert +
                ", bohrVertCross=" + bohrVertCross +
                ", cutting=" + cutting +
                '}';
    }
}
