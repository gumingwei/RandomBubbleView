package com.yuntu.randombubbleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class RandomBubbleLayout extends LinearLayout {

    private LayoutInflater layoutInflater;

    public RandomBubbleLayout(Context context) {
        this(context, null);
    }

    public RandomBubbleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RandomBubbleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        layoutInflater = LayoutInflater.from(context);
    }

    public void createBubbleTrack(int count) {
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, Utils.dip2px(getContext(), 60));
            addView(inflater(), params);
        }
    }

    private View inflater() {
        return layoutInflater.inflate(R.layout.bubble_track, null);
    }
}
