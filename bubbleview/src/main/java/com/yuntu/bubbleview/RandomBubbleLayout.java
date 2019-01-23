package com.yuntu.bubbleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomBubbleLayout extends LinearLayout {

    private LayoutInflater layoutInflater;

    private float mTrackHeight;

    private Adapter mAdapter;

    public RandomBubbleLayout(Context context) {
        this(context, null);
    }

    public RandomBubbleLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public RandomBubbleLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
        mTrackHeight = getResources().getDimension(R.dimen.bubbletrack_height);
    }

    public void createBubbleTrack(int count) {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (int i = 0; i < count; i++) {
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, (int) mTrackHeight);
            addView(inflater(), params);
        }
    }

    public void initInflater() {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
    }

    private BubbleTrack inflater() {
        initInflater();
        return (BubbleTrack) layoutInflater.inflate(R.layout.bubble_track, null);
    }

    /**
     * 根据text的长度在bubblelayout中查找空隙列表
     *
     * @param text
     * @return
     */
    public synchronized List<BallisticGap> findBallisticGaps(String text) {
        List<BallisticGap> blankList = new ArrayList<>();
        int trackCount = getChildCount();
        for (int row = 0; row < trackCount; row++) {
            BubbleTrack track = (BubbleTrack) getChildAt(row);
            int count = track.getChildCount();
            int textLength = text.length() * 40 + Utils.dip2px(getContext(), 60);
            if (count == 0) {
                int left = 0;
                int right = track.getMeasuredWidth();
                BallisticGap blank = new BallisticGap(row, left, right, textLength, 0);
                blankList.add(blank);
            } else {
                for (int i = 0; i <= count; i++) {
                    if (i == 0) {
                        View item = track.getChildAt(i);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.getLayoutParams();
                        int left = 0;
                        int right = layoutParams.leftMargin;
                        if ((right - left) > textLength) {
                            BallisticGap blank = new BallisticGap(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    } else if (i == count) {
                        View item = track.getChildAt(i - 1);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.getLayoutParams();
                        int left = layoutParams.leftMargin + item.getMeasuredWidth();
                        int right = track.getMeasuredWidth();
                        if ((right - left) > textLength) {
                            BallisticGap blank = new BallisticGap(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    } else {
                        View leftView = track.getChildAt(i - 1);
                        View rightView = track.getChildAt(i);
                        FrameLayout.LayoutParams leftParams = (FrameLayout.LayoutParams) leftView.getLayoutParams();
                        FrameLayout.LayoutParams rightParams = (FrameLayout.LayoutParams) rightView.getLayoutParams();
                        int left = leftParams.leftMargin + leftView.getMeasuredWidth();
                        int right = rightParams.leftMargin;
                        if ((right - left) > textLength) {
                            BallisticGap blank = new BallisticGap(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    }
                }
            }
        }
        return blankList;
    }

    Random random = new Random();

    /**
     * 给layout随机添加item
     *
     * @param list
     * @param text
     */
    public synchronized void addItem(String text, List<BallisticGap> list) {
        View view = null;
        if (mAdapter != null) {
            view = mAdapter.getView(text);
        }
        if (view == null) {
            return;
        }
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        BallisticGap blank = list.get(random.nextInt(list.size()));
        int row = blank.getRow();
        BubbleTrack bubbleTrack = (BubbleTrack) getChildAt(row);
        int top = random.nextInt(Utils.dip2px(getContext(), 20));
        if (bubbleTrack.getChildCount() == 0) {
            int bound = blank.getRight() - blank.getTargetTextwidth();
            if (bound > 0) {
                int left = random.nextInt(bound);
                params.leftMargin = left;
                params.topMargin = top;
                bubbleTrack.addView(view, params);
                if (mAdapter != null) {
                    mAdapter.showView(view);
                }
            }
        } else {
            if (blank.getIndex() > 0) {
                View lastView = bubbleTrack.getChildAt(blank.getIndex() - 1);
                if (lastView == null) {
                    return;
                }
                FrameLayout.LayoutParams lastViewParams = (FrameLayout.LayoutParams) lastView.getLayoutParams();
                if (lastViewParams == null) {
                    return;
                }
                int lastViewSize = lastViewParams.leftMargin + lastView.getMeasuredWidth();
                int bound = blank.getRight() - blank.getTargetTextwidth() - lastViewSize;
                if (bound > 0) {
                    int left = lastViewSize + random.nextInt(bound);
                    params.leftMargin = left;
                    params.topMargin = top;
                    bubbleTrack.addView(view, blank.getIndex(), params);
                    if (mAdapter != null) {
                        mAdapter.showView(view);
                    }
                }
            } else {
                int bound = blank.getRight() - blank.getTargetTextwidth();
                if (bound > 0) {
                    int left = random.nextInt(bound);
                    params.leftMargin = left;
                    params.topMargin = top;
                    bubbleTrack.addView(view, blank.getIndex(), params);
                    if (mAdapter != null) {
                        mAdapter.showView(view);
                    }
                }
            }
        }
    }

    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }
}
