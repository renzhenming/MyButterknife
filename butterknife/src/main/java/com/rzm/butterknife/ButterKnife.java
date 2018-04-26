package com.rzm.butterknife;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * Created by renzhenming on 2018/4/24.
 */

public class ButterKnife {

    public static Unbinder bind(Activity activity){
        try {
            Class<? extends Unbinder> clazz = (Class<? extends Unbinder>) Class.forName(activity.getClass().getName() + "_ViewBinding");
            //构造函数
            Constructor<? extends Unbinder> constuctor = clazz.getDeclaredConstructor(activity.getClass());
            constuctor.setAccessible(true);
            Unbinder unbinder = constuctor.newInstance(activity);
            return unbinder;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Unbinder.EMPTY;
    }
}
