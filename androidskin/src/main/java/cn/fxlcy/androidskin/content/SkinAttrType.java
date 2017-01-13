package cn.fxlcy.androidskin.content;

import android.view.View;

/**
 * Created by fxlcy on 2016/11/5.
 */

public abstract class SkinAttrType {
    String mSkinAttrName;


    public SkinAttrType(String attrName) {
        mSkinAttrName = attrName;
    }

    void setAttrValue(View view,String resName) {
        setAttrValue(view,mSkinAttrName,resName);
    }

    protected abstract void setAttrValue(View view, String attrName, String resName);
}
