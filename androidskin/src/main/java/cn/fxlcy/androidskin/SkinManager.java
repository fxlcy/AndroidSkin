package cn.fxlcy.androidskin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import cn.fxlcy.androidskin.callback.SkinChangedListener;
import cn.fxlcy.androidskin.config.SkinSpConfig;
import cn.fxlcy.androidskin.content.SkinAttr;
import cn.fxlcy.androidskin.content.SkinAttrView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.WeakHashMap;

/**
 * Created by fxlcy on 2016/11/5.
 */

public final class SkinManager {
    private static SkinManager mInstance;

    private ResourceManager mResManager;
    private boolean mIsUseSkin = false;

    private ArrayList<SkinChangedListener> mSkinChangedListeners;
    private WeakHashMap<SkinChangedListener, SkinAttrViewList> mSkins;

    private SkinManager() {
    }

    static SkinManager getInstance(ResourceManager resourceManager) {
        getInstance();
        if (resourceManager == null) {
            if (mInstance.mResManager == null) {
                throw new NullPointerException("resourceManager == NULL");
            }
        } else if (!resourceManager.equals(mInstance.mResManager)) {
            mInstance.mResManager = resourceManager;
            mInstance.mIsUseSkin = true;
            //如果resourceManager发生改变，通知所有SkinChangedListeners
            mInstance.notificationSkinChanged();
        }

        return mInstance;
    }

    public static SkinManager getInstance() {
        if (mInstance == null) {
            mInstance = new SkinManager();
        }

        return mInstance;
    }

    public ResourceManager getResourceManager() {
        return mResManager;
    }

    public boolean isUseSkin() {
        return mIsUseSkin;
    }

    /**
     * 通知皮肤改变
     */
    public void notificationSkinChanged() {
        if (mSkinChangedListeners != null) {
            for (SkinChangedListener listener : mSkinChangedListeners) {
                setSkinAttrValue(listener);
                listener.onSkinChanged();
            }
        }
    }

    //重置回原来的theme
    public void restoreDefaultTheme(Context context) {
        SkinSpConfig.getInstance(context).clear();
        mIsUseSkin = false;
        this.mResManager = new ResourceManager(context.getResources(),
                context.getPackageName());
        notificationSkinChanged();
    }

    public void clearSkinAttrs() {
        if (mSkins != null) {
            mSkins.clear();
        }
    }

    public void removeSkinAttrsByView(SkinChangedListener listener, View view) {
        SkinAttrViewList skinAttrViews = mSkins.get(listener);
        SkinAttrView skinAttrView;
        if ((skinAttrView = getSkinAttrViewByView(skinAttrViews, view)) != null) {
            skinAttrViews.remove(skinAttrView);
        }

    }

    public void addSkinChangedListener(SkinChangedListener listener) {
        if (mSkinChangedListeners == null) {
            mSkinChangedListeners = new ArrayList<>();
        }
        mSkinChangedListeners.add(listener);
    }

    public void removeSkinChangedListener(SkinChangedListener listener) {
        if (mSkinChangedListeners != null) {
            mSkinChangedListeners.remove(listener);
        }
        if (mSkins != null) {
            mSkins.remove(listener);
        }
    }

    public void addSkinAttrViews(SkinChangedListener listener, SkinAttrView skinAttrView) {
        if (mSkins == null) {
            mSkins = new WeakHashMap<>();
        }

        SkinAttrViewList skinAttrViews;
        if ((skinAttrViews = mSkins.get(listener)) == null) {
            skinAttrViews = new SkinAttrViewList();
            mSkins.put(listener, skinAttrViews);
        }

        skinAttrViews.add(skinAttrView);
    }

    public void addSkinAttrView(SkinChangedListener listener, View view, SkinAttr skinAttr) {
        if (mSkins == null) {
            mSkins = new WeakHashMap<>();
            initSkin(listener, skinAttr, view);
        } else {
            SkinAttrViewList skinAttrViews = mSkins.get(listener);
            if (skinAttrViews != null) {
                List<SkinAttr> attrs = null;
                SkinAttrView skinAttrView;
                if ((skinAttrView = getSkinAttrViewByView(skinAttrViews, view)) != null) {
                    attrs = skinAttrView.getSkinAttrs();
                }
                if (attrs == null) {
                    attrs = new ArrayList<>();
                    attrs.add(skinAttr);
                    skinAttrViews.add(new SkinAttrView(view, attrs));
                } else {
                    attrs.add(skinAttr);
                }
            } else {
                initSkin(listener, skinAttr, view);
            }
        }
    }

    public void setSkinAttrValue(SkinChangedListener listener) {
        SkinAttrViewList skinAttrViews = mSkins.get(listener);

        int i = skinAttrViews.size() - 1;
        for (; i >= 0; i--) {
            SkinAttrView skinAttrView = skinAttrViews.get(i);
            if (skinAttrView.isDetached()) {
                skinAttrViews.remove(skinAttrView);
            } else {
                skinAttrView.setSkinAttrValue();
            }
        }
    }

    public List<SkinAttrView> getSkinViews(SkinChangedListener listener) {
        return mSkins.get(listener);
    }


    /**
     * 更改同一个皮肤包中的皮肤（改变皮肤后缀）
     */
    public void changePackageSkin(Context context, String suffix) {
        SkinSpConfig.getInstance(context)
                .putSkinSuffix(suffix);
        notificationSkinChanged();
    }

    private void initSkin(SkinChangedListener listener, SkinAttr skinAttr, View view) {
        SkinAttrViewList skinAttrViews = new SkinAttrViewList();
        ArrayList<SkinAttr> attrs = new ArrayList<>();
        attrs.add(skinAttr);
        skinAttrViews.add(new SkinAttrView(view, attrs));
        mSkins.put(listener, skinAttrViews);
    }

    private SkinAttrView getSkinAttrViewByView(SkinAttrViewList skinAttrViews, View view) {
        for (SkinAttrView skinAttrView : skinAttrViews) {
            if (skinAttrView.equalsView(view)) {
                return skinAttrView;
            }
        }

        return null;
    }

    private static class SkinAttrViewList extends LinkedList<SkinAttrView> {
        @Override
        public boolean add(SkinAttrView object) {
            boolean b = super.add(object);
            recyclerSkinView();
            return b;
        }

        @Override
        public void add(int index, SkinAttrView object) {
            super.add(index, object);
            recyclerSkinView();
        }

        @Override
        public boolean addAll(Collection<? extends SkinAttrView> collection) {
            boolean b = super.addAll(collection);
            recyclerSkinView();
            return b;
        }

        @Override
        public boolean addAll(int index, Collection<? extends SkinAttrView> collection) {
            boolean b = super.addAll(index, collection);
            recyclerSkinView();
            return b;
        }

        @Override
        public boolean remove(Object object) {
            boolean b = super.remove(object);
            recyclerSkinView();
            return b;
        }

        @Override
        public SkinAttrView remove(int index) {
            SkinAttrView b = super.remove(index);
            recyclerSkinView();
            return b;
        }

        @Override
        protected void removeRange(int fromIndex, int toIndex) {
            super.removeRange(fromIndex, toIndex);
            recyclerSkinView();
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> collection) {
            boolean b = super.removeAll(collection);
            recyclerSkinView();
            return b;
        }

        private void recyclerSkinView() {
            ListIterator<SkinAttrView> iterator = this.listIterator();
            while (iterator.hasNext()) {
                SkinAttrView sav = iterator.next();
                if (sav.isDetached()) {
                    iterator.remove();
                }
            }
        }
    }
}
