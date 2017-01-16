package cn.fxlcy.androidskin.util;

import android.content.res.ColorStateList;
import android.view.View;
import android.widget.TextView;

import cn.fxlcy.androidskin.ResourceManager;
import cn.fxlcy.androidskin.SkinManager;

/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinUtils {
    private SkinUtils() {
        throw new RuntimeException("Stub");
    }

    public static void setTextColorResource(View view, String resName) {
        if (view instanceof TextView || view instanceof TextViewCompat) {
            ResourceManager rm = SkinManager.getInstance().getResourceManager();
            ColorStateList colorStateList = rm.getColorStateList(resName);
            int color;
            if (colorStateList == null) {
                color = rm.getColor(resName);
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(color);
                } else {
                    ((TextViewCompat) view).setTextColor(color);
                }
            } else {
                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(colorStateList);
                } else {
                    ((TextViewCompat) view).setTextColor(colorStateList);
                }
            }
        }
    }


}
