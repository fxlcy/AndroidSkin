package cn.fxlcy.androidskin.util;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public final class AppUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mAppContext;


    /**
     * 获得ApplicationContext
     */
    public static Context getApplicationContext(Context context) {
        if (mAppContext != null) {
            return mAppContext;
        }
        if (context instanceof Application) {
            mAppContext = context;
            return context;
        } else {
            try {
                mAppContext = context.getApplicationContext();
                return mAppContext;
            } catch (Exception e) {
                e.printStackTrace();
                return context;
            }
        }
    }
}
