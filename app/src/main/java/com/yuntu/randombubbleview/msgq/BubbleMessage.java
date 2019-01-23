package com.yuntu.randombubbleview.msgq;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class BubbleMessage extends Queue {

    private boolean isTake = true;

    private boolean isPause;

    private int TIME_INTERVAL = 500;

    private List<BubbleMsgListener> mListeners;

    private OnInfoListener mInfoListener;

    public interface BubbleMsgListener extends QListener {

        /**
         * 通过接口返回队列的第一个元素提供给外界使用
         *
         * @param firstMsg
         * @return
         */
        boolean isTake(String firstMsg);
    }

    public interface OnInfoListener {

        void onMessageCount(int count);

        void onTaskCount(int count);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    if (mListeners != null && mListeners.size() > 0) {
                        if (mListeners.get(msg.arg1) != null && msg.obj != null) {
                            mListeners.get(msg.arg1).onTake((String) msg.obj);
                        }
                    }
                    if (mInfoListener != null) {
                        mInfoListener.onTaskCount(mListeners.size());
                    }
                    break;
                case 0x02:
                    if (mInfoListener != null) {
                        mInfoListener.onMessageCount(msg.arg1);
                    }
                    break;
            }
        }
    };

    @Override
    public void put(Object arg) {
        super.put(arg);
        sendQueueSize();
    }

    private void sendQueueSize() {
        if (mInfoListener != null) {
            Message countMessage = new Message();
            countMessage.what = 0x02;
            countMessage.arg1 = queue.size();
            handler.sendMessage(countMessage);
        }
    }

    private void take(BubbleMsgListener listener, int index) {
        while (isTake) {
            try {
                if (!isPause) {
                    if (peek() != null) {
                        if (listener.isTake((String) peek())) {
                            //取消息
                            Message message = new Message();
                            message.what = 0x01;
                            message.obj = take();
                            message.arg1 = index;
                            handler.sendMessage(message);
                            //消息队列size
                            sendQueueSize();
                        }
                    }
                }
                Thread.sleep(TIME_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void addMessageTaker(final BubbleMsgListener listener) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        final int index = mListeners.size();
        mListeners.add(listener);
        isTake = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                take(listener, index);
            }
        }).start();
    }

    public void setOnInfoListener(OnInfoListener listener) {
        mInfoListener = listener;
    }

    public void removeListener() {
        mListeners.clear();
    }

    public void pause() {
        isPause = true;
    }

    public void resume() {
        isPause = false;
    }

    @Override
    public void stop() {
        isTake = false;
        removeListener();
    }
}
