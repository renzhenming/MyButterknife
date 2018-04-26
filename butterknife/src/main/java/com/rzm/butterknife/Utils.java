package com.rzm.butterknife;

import android.app.Activity;
import android.view.View;

/**
 * Created by renzhenming on 2018/4/26.
 */

public class Utils {
    public static <T extends View> T findViewById(Activity activity,int viewId){
        return (T)activity.findViewById(viewId);
    }
}
