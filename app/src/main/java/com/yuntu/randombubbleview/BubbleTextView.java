package com.yuntu.randombubbleview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class BubbleTextView extends AppCompatTextView {

    public BubbleTextView(Context context) {
        this(context, null);
    }

    public BubbleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showAnimator() {
        ObjectAnimator showAlpha = ObjectAnimator.ofFloat(this, "alpha", 0, 1).setDuration(200);
        ObjectAnimator showTransY = ObjectAnimator.ofFloat(this, "translationY", 20, 0).setDuration(200);
        ObjectAnimator hideAnima = ObjectAnimator.ofFloat(this, "alpha", 1, 0).setDuration(200);
        ObjectAnimator hideTransY = ObjectAnimator.ofFloat(this, "translationY", 0, 20).setDuration(200);
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
                if (getParent() != null) {
                    ViewGroup viewGroup = (ViewGroup) getParent();
                    viewGroup.removeView(BubbleTextView.this);
                }
            }
        });
        animatorSet.start();
    }
}
