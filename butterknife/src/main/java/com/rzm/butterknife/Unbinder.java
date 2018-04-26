package com.rzm.butterknife;

import android.support.annotation.UiThread;

/**
 * Created by renzhenming on 2018/4/26.
 */

public interface Unbinder {

    //仿照butterknife源码

    @UiThread
    void unbind();

    Unbinder EMPTY = new Unbinder() {
        @Override
        public void unbind() {

        }
    };
}
