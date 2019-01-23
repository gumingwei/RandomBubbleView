package com.yuntu.bubbleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;

public class AnimatorUtils {

    public static void showAnimator(final View view, final Callback callback) {
        ObjectAnimator showAlpha = ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(200);
        ObjectAnimator showTransY = ObjectAnimator.ofFloat(view, "translationY", 20, 0).setDuration(200);
        ObjectAnimator hideAnima = ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(200);
        ObjectAnimator hideTransY = ObjectAnimator.ofFloat(view, "translationY", 0, 20).setDuration(200);
        hideAnima.setStartDelay(6000);
        hideTransY.setStartDelay(6000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(showAlpha).with(showTransY);
        animatorSet.play(hideAnima).with(hideTransY);
        hideAnima.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation, boolean isReverse) {

            }

            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                if (view.getParent() != null) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    viewGroup.removeView(view);
                    if (callback != null) {
                        callback.onAnimaEnd();
                    }
                }
            }
        });
        animatorSet.start();
    }
}
