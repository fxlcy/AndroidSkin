package cn.fxlcy.androidskin.content;

import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinAttrView {
    private WeakReference<View> mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinAttrView(final View view, List<SkinAttr> attrs) {
        mView = new WeakReference<>(view);
        mSkinAttrs = attrs;
    }

    public void setSkinAttrValue() {
        for (SkinAttr attr : mSkinAttrs) {
            attr.setSkinAttrValue(mView.get());
        }
    }

    public List<SkinAttr> getSkinAttrs() {
        return mSkinAttrs;
    }

    public boolean isDetached(){
        return mView.get() == null;
    }

    public boolean equalsView(View view) {
        return view.equals(mView.get());
    }
}
