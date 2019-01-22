package com.yuntu.randombubbleview.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yuntu.randombubbleview.Adapter;
import com.yuntu.randombubbleview.AnimatorUtils;
import com.yuntu.randombubbleview.BallisticGap;
import com.yuntu.randombubbleview.BubbleTextView;
import com.yuntu.randombubbleview.BubbleTrack;
import com.yuntu.randombubbleview.Callback;
import com.yuntu.randombubbleview.R;
import com.yuntu.randombubbleview.RandomBubbleLayout;
import com.yuntu.randombubbleview.msgq.BubbleMessage;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView mQueueCount;

    TextView mTaskCount;

    TextView mViewpool;

    RandomBubbleLayout bubbleLayout;

    LayoutInflater mInflate;

    Random random = new Random();

    String text[] = new String[]{
            "你好老铁"
            , "啊"
            , "来了老弟"
            , "我也想低调啊"
            , "可是实力不允许啊"
            , "哈哈"
            , "实力不允许啊"
            , "盘他"
            , "你说什么"
            , "安排上了"
            , "习近平：提高防控能力保持经济社会大局稳定"
            , "因为你归心似箭！"
            , "拼多多优惠券成被薅羊毛那些券用返还吗？"
            , "红红火火中国年"};

    BubbleMessage bubbleMessage = new BubbleMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInflate = LayoutInflater.from(getBaseContext());
        setContentView(R.layout.activity_main);
        mQueueCount = findViewById(R.id.queue);
        mTaskCount = findViewById(R.id.task);
        mViewpool = findViewById(R.id.viewpool);

        bubbleLayout = findViewById(R.id.bubblelayout);
        bubbleLayout.createBubbleTrack(4);
        bubbleLayout.setAdapter(new BubbleAdapter(getBaseContext()));

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textIndex = text[random.nextInt(text.length)];
                bubbleMessage.put(textIndex);
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
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubbleMessage.addMessageTaker(new BubbleMessage.BubbleMsgListener() {

                    private List<BallisticGap> list;

                    @Override
                    public boolean isTake(String firstMsg) {

                        list = bubbleLayout.findBallisticGaps(firstMsg);
                        if (null != list && list.size() > 0) {
                            return true;
                        }
                        return false;
                    }

                    @Override
                    public void onTake(String arg) {
                        if (null != list && list.size() > 0) {
                            bubbleLayout.addItem(arg, list);
                        }
                    }
                });
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubbleMessage.stop();
            }
        });

        for (int i = 0; i < 1000; i++) {
            bubbleMessage.put(text[random.nextInt(text.length)]);
        }

        bubbleMessage.setOnInfoListener(new BubbleMessage.OnInfoListener() {

            @Override
            public void onMessageCount(int count) {
                mQueueCount.setText("实时消息队列:" + count);
            }

            @Override
            public void onTaskCount(int count) {
                mTaskCount.setText("实时任务数:" + count);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        bubbleMessage.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bubbleMessage.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bubbleMessage.stop();
    }

    public class BubbleAdapter extends Adapter {

        public BubbleAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(String arg) {
            BubbleTextView child = (BubbleTextView) super.getView(arg);
            child.setText(arg);
            child.setTextColor(Color.parseColor("#ffffff"));
            return child;
        }

        @Override
        public int createChildView() {
            return R.layout.bubble_item;
        }

        @Override
        public void showView(final View view) {
            AnimatorUtils.showAnimator(view, new Callback() {
                @Override
                public void onAnimaEnd() {
                    recyleView(view);
                }
            });
        }

        @Override
        public void getViewPoolList(int size) {
            super.getViewPoolList(size);
            mViewpool.setText("View复用池实时个数:" + size);
        }
    }
}
