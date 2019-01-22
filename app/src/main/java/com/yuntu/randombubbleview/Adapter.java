package com.yuntu.randombubbleview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public abstract class Adapter {

    private List<View> viewPoolList;

    private LayoutInflater mInflater;

    public Adapter(Context context) {
        this.viewPoolList = new LinkedList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    public List<View> getViewList() {
        return viewPoolList;
    }

    public View getView(String arg) {
        View child;
        if (viewPoolList.size() > 0) {
            child = viewPoolList.remove(0);
        } else {
            child = mInflater.inflate(createChildView(), null);
        }
        getViewPoolList(viewPoolList.size());
        return child;
    }

    /**
     * 回收View
     *
     * @param view
     */
    public void recyleView(View view) {
        viewPoolList.add(view);
        getViewPoolList(viewPoolList.size());
    }

    public void getViewPoolList(int size) {

    }

    public abstract int createChildView();

    public abstract void showView(View view);
}
