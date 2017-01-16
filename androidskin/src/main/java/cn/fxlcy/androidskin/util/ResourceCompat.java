package cn.fxlcy.androidskin.util;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.TypedValue;

/**
 * Created by fxlcy on 2016/11/4.
 */

public class ResourceCompat {
    private static final Object mResourceLock = new Object();
    private static TypedValue mTempValue = null;

    private ResourceCompat() {
        throw new RuntimeException("Stub");
    }

    public static Drawable getDrawable(Resources res, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return res.getDrawable(resId, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return res.getDrawable(resId);
        } else {
            final int resolvedId;

            synchronized (mResourceLock) {
                if (mTempValue == null) {
                    mTempValue = new TypedValue();
                }
                res.getValue(resId, mTempValue, true);
                resolvedId = mTempValue.resourceId;
            }
            return res.getDrawable(resolvedId);
        }
    }

    public static int getColor(Resources res, @ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return res.getColor(resId, null);
        } else {
            return res.getColor(resId);
        }
    }

    public static ColorStateList getColorStateList(Resources res, @ColorRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return res.getColorStateList(resId, null);
        } else {
            return res.getColorStateList(resId);
        }
    }
}
