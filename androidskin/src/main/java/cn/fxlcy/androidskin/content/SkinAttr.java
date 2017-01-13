package cn.fxlcy.androidskin.content;

import android.view.View;

/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinAttr {
    private SkinAttrType mType;
    private String mSkinAttrRes;

    public SkinAttr(SkinAttrType type, String attrRes) {
        mType = type;
        mSkinAttrRes = attrRes;
    }

    void setSkinAttrValue(View view) {
        mType.setAttrValue(view, mSkinAttrRes);
    }

    @Override
    public String toString() {
        return String.format("{SkinAttrName:%s,SkinAttrResName:%s}"
                , mType.mSkinAttrName, mSkinAttrRes);
    }
}
