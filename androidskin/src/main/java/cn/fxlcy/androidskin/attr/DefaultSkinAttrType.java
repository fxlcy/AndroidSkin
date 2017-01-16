package cn.fxlcy.androidskin.attr;

import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.view.View;

import cn.fxlcy.androidskin.ResourceManager;
import cn.fxlcy.androidskin.SkinManager;
import cn.fxlcy.androidskin.annotation.ResourceType;
import cn.fxlcy.androidskin.content.SkinAttrType;
import cn.fxlcy.androidskin.util.SkinUtils;


/**
 * Created by fxlcy on 2016/11/5.
 */

public class DefaultSkinAttrType extends SkinAttrType {
    private final static String BACKGROUND = "background";
    private final static String TEXT_COLOR = "textColor";

    private DefaultSkinAttrType(String attrName) {
        super(attrName);
    }

    @Override
    protected void setAttrValue(View view, String attrName, String resName) {
        ResourceManager rm = SkinManager.getInstance().getResourceManager();
        switch (attrName) {
            case BACKGROUND:
                Drawable drawable = rm.getDrawable(resName);

                if (drawable != null) {
                    ViewCompat.setBackground(view, drawable);
                }
                break;
            case TEXT_COLOR:
                int id = rm.getIdentifierAttachSuffix(resName, ResourceType.COLOR);
                if (id != 0) {
                    SkinUtils.setTextColorResource(view, resName);
                }
                break;
        }
    }

    public final static SkinAttrType SKINATTRTYPE_BACKGROUND = new DefaultSkinAttrType(BACKGROUND);
    public final static SkinAttrType SKINATTRTYPE_TEXTCOLOR = new DefaultSkinAttrType(TEXT_COLOR);
}
