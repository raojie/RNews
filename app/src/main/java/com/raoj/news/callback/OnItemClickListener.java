package com.raoj.news.callback;

import android.view.View;

/**
 * class: OnItemClickListener
 * describe:点击长按的接口
 * author: raoj
 * date: 2018-09-07-16:25
 */
public interface OnItemClickListener {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
}
