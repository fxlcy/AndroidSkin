package cn.fxlcy.androidskin.content;

import java.util.HashMap;

import static cn.fxlcy.androidskin.attr.DefaultSkinAttrType.SKINATTRTYPE_BACKGROUND;
import static cn.fxlcy.androidskin.attr.DefaultSkinAttrType.SKINATTRTYPE_TEXTCOLOR;


/**
 * Created by fxlcy on 2016/11/5.
 */

public final class SkinAttrManager {
    private static SkinAttrManager mInstance;

    public static SkinAttrManager getInstance() {
        if (mInstance == null) {
            mInstance = new SkinAttrManager();
        }

        return mInstance;
    }

    private SkinAttrManager() {
        sSkinAttrs = new HashMap<>();
        putSkinAttr(SKINATTRTYPE_BACKGROUND);
        putSkinAttr(SKINATTRTYPE_TEXTCOLOR);
    }


    private HashMap<String, SkinAttrType> sSkinAttrs;

    public SkinAttrType getSkinAttr(String attrName) {
        return sSkinAttrs.get(attrName);
    }

    public void putSkinAttr(SkinAttrType skinAttrType) {
        sSkinAttrs.put(skinAttrType.mSkinAttrName,skinAttrType);
    }

    public boolean containsSkinAttr(String attrName) {
        return sSkinAttrs.containsKey(attrName);
    }
}
