package com.yuntu.randombubbleview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.yuntu.randombubbleview.BallisticGap;
import com.yuntu.randombubbleview.BubbleTrack;
import com.yuntu.randombubbleview.R;
import com.yuntu.randombubbleview.RandomBubbleLayout;
import com.yuntu.randombubbleview.msgq.BubbleMessage;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView mQueueCount;

    TextView mTaskCount;

    RandomBubbleLayout bubbleLayout;

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
        setContentView(R.layout.activity_main);
        mQueueCount = findViewById(R.id.queue);
        mTaskCount = findViewById(R.id.task);

        bubbleLayout = findViewById(R.id.bubblelayout);
        bubbleLayout.createBubbleTrack(4);
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*int textIndex = random.nextInt(text.length);
                List<BallisticGap> list = bubbleLayout.findBallisticGaps(text[textIndex]);
                if (null != list && list.size() > 0) {
                    bubbleLayout.addItem(list, text[textIndex]);
                }*/
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
                bubbleMessage.removeListener();
            }
        });
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bubbleMessage.addMessageTaker(new BubbleMessage.BubbleMsgListener() {

                    private List<BallisticGap> list;

                    @Override
                    public boolean isTake(String firstMsg) {
                        Log.i("Gmw", "isTake=" + firstMsg);
                        synchronized (bubbleLayout) {
                            list = bubbleLayout.findBallisticGaps(firstMsg);
                            if (null != list && list.size() > 0) {
                                return true;
                            }
                            return false;
                        }
                    }

                    @Override
                    public void onTake(String arg) {
                        synchronized (bubbleLayout) {
                            Log.i("Gmw", "onTake=" + arg);
                            if (null != list && list.size() > 0) {
                                bubbleLayout.addItem(list, arg);
                            }
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

    private void print(List<BallisticGap> list) {
        if (null != list && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Log.i("Gmw", "item_i=" + i + ",item=" + list.get(i).toString());
            }
        } else {
            Log.i("Gmw", "list_null=");
        }
    }
}
