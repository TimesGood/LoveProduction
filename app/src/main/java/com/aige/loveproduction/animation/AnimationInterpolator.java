package com.aige.loveproduction.animation;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/**
 * 自定义一些动画插值器
 */
public class AnimationInterpolator {

    /**
     * 果冻效果插值器
     */
    public static class JellyInterpolator implements Interpolator {
        private final float factor = 0.15f;

        @Override
        public float getInterpolation(float input) {
            return (float) (Math.pow(2, -10 * input) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
        }
    }
    public static class DampInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            float move = (1 - input);
            return 1 - (float) Math.pow(move, 3);
        }
    }
}
