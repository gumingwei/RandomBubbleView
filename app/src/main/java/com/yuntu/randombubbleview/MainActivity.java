package com.yuntu.randombubbleview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RandomBubbleLayout bubbleLayout;

    Random random = new Random();

    String text[] = new String[]{
            "你好老铁"
            , "啊"
            , "来了老弟"
            , "我也想低调啊"
            , "可是实力不允许啊"
            , "哈哈"
            , "一giao窝里giaogiao"
            , "盘他"
            , "你说什么"
            , "安排上了"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubbleLayout = findViewById(R.id.bubblelayout);
        bubbleLayout.createBubbleTrack(5);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView mTextView = (TextView) LayoutInflater.from(getBaseContext()).inflate(R.layout.bubble_item, null);
                int textIndex = random.nextInt(text.length);
                mTextView.setText(text[textIndex]);
                mTextView.setTextColor(Color.parseColor("#ffffff"));
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                List<BallisticBlank> list = findBlank(mTextView.getText().toString());
                if (null != list && list.size() > 0) {
                    BallisticBlank blank = list.get(random.nextInt(list.size()));
                    int row = blank.getRow();
                    BubbleTrack bubbleTrack = (BubbleTrack) bubbleLayout.getChildAt(row);
                    int top = random.nextInt(Utils.dip2px(getBaseContext(), 30));
                    if (bubbleTrack.getChildCount() == 0) {
                        int left = random.nextInt(blank.getRight() - blank.getTargetTextwidth());
                        params.leftMargin = left;
                        params.topMargin = top;
                        bubbleTrack.addView(mTextView, params);
                    } else {
                        if (blank.getIndex() > 0) {
                            View lastView = bubbleTrack.getChildAt(blank.getIndex() - 1);
                            FrameLayout.LayoutParams lastViewParams = (FrameLayout.LayoutParams) lastView.getLayoutParams();
                            int lastViewSize = lastViewParams.leftMargin + lastView.getMeasuredWidth();
                            int left = lastViewSize + random.nextInt(blank.getRight() - blank.getTargetTextwidth() - lastViewSize);
                            params.leftMargin = left;
                            params.topMargin = top;
                            bubbleTrack.addView(mTextView, blank.getIndex(), params);
                        } else {
                            int left = random.nextInt(blank.getRight() - blank.getTargetTextwidth());
                            params.leftMargin = left;
                            params.topMargin = top;
                            bubbleTrack.addView(mTextView, blank.getIndex(), params);
                        }
                    }
                }

            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < bubbleLayout.getChildCount(); i++) {
                    BubbleTrack bubbleTrack = (BubbleTrack) bubbleLayout.getChildAt(i);
                    bubbleTrack.removeAllViews();
                }
            }
        });
    }

    private List<BallisticBlank> findBlank(String text) {
        List<BallisticBlank> blankList = new ArrayList<>();
        int trackCount = bubbleLayout.getChildCount();
        for (int row = 0; row < trackCount; row++) {
            BubbleTrack track = (BubbleTrack) bubbleLayout.getChildAt(row);
            int count = track.getChildCount();
            int textLength = text.length() * 40 + Utils.dip2px(getBaseContext(), 20);
            if (count == 0) {
                int left = 0;
                int right = track.getMeasuredWidth();
                BallisticBlank blank = new BallisticBlank(row, left, right, textLength, 0);
                blankList.add(blank);
            } else {
                for (int i = 0; i <= count; i++) {
                    if (i == 0) {
                        View item = track.getChildAt(i);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.getLayoutParams();
                        int left = 0;
                        int right = layoutParams.leftMargin;
                        if ((right - left) > textLength) {
                            BallisticBlank blank = new BallisticBlank(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    } else if (i == count) {
                        View item = track.getChildAt(count - 1);
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) item.getLayoutParams();
                        int left = layoutParams.leftMargin + item.getMeasuredWidth();
                        int right = track.getMeasuredWidth();
                        if ((right - left) > textLength) {
                            BallisticBlank blank = new BallisticBlank(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    } else {
                        TextView leftView = (TextView) track.getChildAt(i - 1);
                        TextView rightView = (TextView) track.getChildAt(i);
                        FrameLayout.LayoutParams leftParams = (FrameLayout.LayoutParams) leftView.getLayoutParams();
                        FrameLayout.LayoutParams rightParams = (FrameLayout.LayoutParams) rightView.getLayoutParams();
                        int left = leftParams.leftMargin + leftView.getMeasuredWidth();
                        int right = rightParams.leftMargin;
                        if ((right - left) > textLength) {
                            BallisticBlank blank = new BallisticBlank(row, left, right, textLength, i);
                            blankList.add(blank);
                        }
                    }
                }
            }
        }
        return blankList;
    }

    private void print(List<BallisticBlank> list) {
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.i("Gmw", "item_i=" + i + ",item=" + list.get(i).toString());
            }
        } else {
            Log.i("Gmw", "list_null=");
        }
    }
}
