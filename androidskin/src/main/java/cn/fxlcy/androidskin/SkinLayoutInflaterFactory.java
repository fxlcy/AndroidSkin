package cn.fxlcy.androidskin;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.fxlcy.androidskin.callback.SkinChangedListener;
import cn.fxlcy.androidskin.config.SkinConfig;
import cn.fxlcy.androidskin.content.SkinAttr;
import cn.fxlcy.androidskin.content.SkinAttrManager;
import cn.fxlcy.androidskin.content.SkinAttrType;
import cn.fxlcy.androidskin.content.SkinAttrView;
import cn.fxlcy.view.BaseLayoutInflaterFactory;

/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinLayoutInflaterFactory extends BaseLayoutInflaterFactory {
    private SkinChangedListener mSkinChangedListener;

    private final static String ATTR_STYLE_NAME = "style";
    private final static String ATTR_STYLE_VALUE_PREFIX = "@style";
    private final static int ATTR_STYLE_VALUE_PREFIX_LENGTH = ATTR_STYLE_VALUE_PREFIX.length() + 1;

    public SkinLayoutInflaterFactory(SkinChangedListener listener) {
        mSkinChangedListener = listener;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(parent, name, context, attrs);

        int count = attrs.getAttributeCount();
        LinkedList<SkinAttr> skinAttrs = new LinkedList<>();


        for (int i = 0; i < count; i++) {
            String attrName = attrs.getAttributeName(i);
            String value;

            SkinAttrType type;
            //如果该attr不是皮肤的attr
            if ((type = SkinAttrManager.getInstance().getSkinAttr(attrName)) == null) {
                if (attrName.equals(ATTR_STYLE_NAME)) {
                    value = attrs.getAttributeValue(i);
                    if (value.startsWith(ATTR_STYLE_VALUE_PREFIX)) {
                        skinAttrs.addAll(getStyleSkinAttrs(context, attrs, value));
                    }
                }

                continue;
            }

            value = attrs.getAttributeValue(i);

            /**
             * 如果该属性是引用的资源文件
             * */
            if (value.startsWith("@")) {
                int attrId;
                try {
                    attrId = Integer.parseInt(value.substring(1));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    continue;
                }
                if (attrId != 0) {
                    String resName = context.getResources().getResourceEntryName(attrId);
                    if (SkinConfig.SKIN_ATTR_FILTER.filter(resName)) {
                        skinAttrs.add(new SkinAttr(type, resName));
                    }
                }

            }

            /*else if (value.startsWith("?")) {
                String skinAttrName;
                if ((skinAttrName = getDynamicAttrName(context, value)) != null) {
                    skinAttrs.add(new SkinAttr(type, skinAttrName));
                }
            }*/
        }

        if (!skinAttrs.isEmpty()) {
            SkinAttrView skinAttrView = new SkinAttrView(view
                    , new ArrayList<>(skinAttrs));
            if (SkinManager.getInstance().isUseSkin()) {
                skinAttrView.setSkinAttrValue();
            }

            //把attrs添加到skinManager
            SkinManager.getInstance().addSkinAttrViews(mSkinChangedListener, skinAttrView);
        }

        return view;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return onCreateView(null, name, context, attrs);
    }

    /**
     * 获得动态attr(?attr/*)的资源在皮肤中的资源的映射
     */
    private String getDynamicAttrName(Context context, String attrValue) {
        try {
            int id = Integer.parseInt(attrValue.substring(1));
            return context.getResources().getResourceEntryName(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    private List<SkinAttr> getStyleSkinAttrs(Context context, AttributeSet attrs, String styleName) {
        styleName = styleName.substring(ATTR_STYLE_VALUE_PREFIX_LENGTH);

        LinkedList<SkinAttr> skinAttrs = new LinkedList<>();

        Resources resources = context.getResources();
        int styleId = resources.getIdentifier(styleName, ATTR_STYLE_NAME, context.getPackageName());
        TypedArray a = resources.obtainTypedArray(styleId);
        int len = a.length();

        TypedValue tv = new TypedValue();
        for (int i = 0; i < len; i++) {
            a.getValue(i, tv);
            int resId = tv.resourceId;
            if (resId != 0) {
                String name = resources.getResourceEntryName(resId);
                if (SkinConfig.SKIN_ATTR_FILTER.filter(name)) {
                    int li = name.lastIndexOf("_");
                    if (li != -1) {
                        //通过资源文件的后缀获取资源的type
                        String attrName = name.substring(li + 1);
                        SkinAttrType attrType = SkinAttrManager.getInstance().getSkinAttr(attrName);
                        if (attrType != null) {
                            skinAttrs.add(new SkinAttr(attrType, name.substring(0, li)));
                        }
                    }
                }
            }
            /*else {
                CharSequence coerceStr = tv.coerceToString();
                if (coerceStr != null) {
                    String str = coerceStr.toString();
                    if (str.startsWith("?")) {
                        String attrName = getDynamicAttrName(context, str);
                    }
                }
            }*/
        }
        a.recycle();

        return skinAttrs;
    }

}
