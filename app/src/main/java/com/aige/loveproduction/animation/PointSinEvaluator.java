package com.aige.loveproduction.animation;

import android.animation.TypeEvaluator;
import android.graphics.Point;
import android.graphics.PointF;


public class PointSinEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointF startPoint = (PointF) startValue;
        PointF endPoint = (PointF) endValue;
        int x = (int) (startPoint.x + fraction * (endPoint.x - startPoint.x));

        int y = (int) ((int) (Math.sin(x * Math.PI / 180) * 100) + endPoint.y / 2);
        Point point = new Point(x, y);
        return point;
    }
}