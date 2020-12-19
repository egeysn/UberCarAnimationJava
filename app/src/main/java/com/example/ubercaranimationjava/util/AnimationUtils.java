package com.example.ubercaranimationjava.util;


import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

public  class AnimationUtils {


    public static ValueAnimator polylineAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(new int[]{0, 100});
        valueAnimator.setInterpolator((TimeInterpolator)(new LinearInterpolator()));
        valueAnimator.setDuration(4000L);
        return valueAnimator;
    }


    public static ValueAnimator carAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        valueAnimator.setDuration(3000L);
        valueAnimator.setInterpolator((TimeInterpolator)(new LinearInterpolator()));
        return valueAnimator;
    }


}
