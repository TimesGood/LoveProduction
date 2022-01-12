package com.aige.loveproduction.mpr;

import com.aige.loveproduction.enums.MprPosition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MprCuttingData implements MprData{
    private final MprMaster master;
    private final Map<String,Float> map = new HashMap<>();
    private final Map<String,Float> finalMap = new HashMap<>();
    private final List<String> coordText = new ArrayList<>();
    private final StringBuilder builder = new StringBuilder();
    private List<float[]> point;
    private List<float[]> finalPoint;

    public MprCuttingData(MprMaster master) {
        this.master = master;
    }

    private void initPoint(){
        for(int i = 0;i < map.size()/2;i++) {
            Float x = map.get("X"+i);
            Float y = map.get("Y"+i);
            MprPosition position = master.getPosition(x,y);
            switch (position) {
                case LEFT:
                case TOP:
                case RIGHT:
                case BOTTOM:
                    if(point == null) {
                        point = new ArrayList<>();
                        finalPoint = new ArrayList<>();
                    }
                    float[] xy = new float[2];
                    float[] fXY = new float[2];
                    xy[0] = x;
                    xy[1] = y;
                    fXY[0] = x;
                    fXY[1] = y;
                    point.add(xy);
                    finalPoint.add(fXY);
                    break;
            }
        }
    }
    public List<float[]> getPoint(int select){
        if(select == 1) {
            return this.finalPoint;
        }
        return this.point;
    }

    public void addData(String key,Float data) {
        map.put(key,data);
        finalMap.put(key,data);
    }

    public Map<String, Float> getMap() {
        return map;
    }

    /**获取不受缩放影响的数据*/
    public Map<String, Float> getFinalMap() {
        return finalMap;
    }

    @Override
    public void setScale(float scale) {
        map.forEach((k,v) -> this.map.put(k,v*scale));
        if(point != null) {
            point.forEach(v -> {
                v[0] *= scale;
                v[1] *= scale;
            });
        }
    }

    @Override
    public MprData cloneData(MprMaster master) {
        MprCuttingData cuttingModul = new MprCuttingData(master);
        this.map.forEach(cuttingModul::addData);
        cuttingModul.initData();
        return cuttingModul;
    }

    @Override
    public void initData() {
        setCoordText();
        initPoint();
    }
    public String getCoordText(int position) {
        return coordText.get(position);
    }
    private void setCoordText(){
        for(int i = 0 ; i < this.finalMap.size()/2;i++) {
            builder.delete(0,builder.length());
            Float x = this.finalMap.get("X" + i);
            Float y = this.finalMap.get("Y" + i);
            String sx = x % 1.0 == 0 ? String.valueOf(x.intValue()): String.valueOf(x);
            String sy = y % 1.0 == 0 ? String.valueOf(y.intValue()): String.valueOf(y);
            builder.append("x=").append(sx).append(",")
                    .append("y=").append(sy);
            this.coordText.add(i,builder.toString());
        }
    }


    @Override
    public String toString() {
        return "MprCuttingModul{" +
                "map=" + map +
                '}';
    }

}
