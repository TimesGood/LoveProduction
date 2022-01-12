package com.aige.loveproduction.mpr;

import com.aige.loveproduction.enums.MprPosition;

public interface MprData {
    void setScale(float scale);
    MprData cloneData(MprMaster master);
    default void parsePosition(){}
    default MprPosition getPosition(){return MprPosition.NULL;}
    void initData();
}
