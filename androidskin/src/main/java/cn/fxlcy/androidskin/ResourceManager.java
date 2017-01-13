package cn.fxlcy.androidskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import cn.fxlcy.androidskin.annotation.ResourceType;
import cn.fxlcy.androidskin.util.ResourceCompat;

/**
 * Created by fxlcy on 2016/11/4.
 */

public final class ResourceManager {
    private final static String TAG = "ResourceManager";

    private Resources mResource;
    private String mPackageName;
    private String mSuffix;

    public ResourceManager(Resources res, String packageName, String suffix) {
        this(res, packageName);
        mSuffix = suffix;
    }

    public ResourceManager(Resources res, String packageName) {
        mResource = res;
        mPackageName = packageName;
    }

    public void setSuffix(String suffix) {
        mSuffix = suffix;
    }

    public Resources getResource() {
        return mResource;
    }

    public String getPackageName() {
        return mPackageName;
    }


    public Drawable getDrawable(@DrawableRes int id) {
        return ResourceCompat.getDrawable(mResource, id);
    }


    public Drawable getDrawable(String name) {
        int id = getIdentifierAttachSuffix(name, ResourceType.DRAWABLE);
        if (id == 0) {
            id = getIdentifierAttachSuffix(name, ResourceType.COLOR);
        }

        return getDrawable(id);
    }

    public String getString(@StringRes int id) {
        return mResource.getString(id);
    }

    public int getColor(@ColorRes int id) {
        return ResourceCompat.getColor(mResource, id);
    }

    public int getColor(String name) {
        return getColor(getIdentifierAttachSuffix(name, ResourceType.COLOR));
    }

    public ColorStateList getColorStateList(@ColorRes int id) {
        return ResourceCompat.getColorStateList(mResource, id);
    }

    public ColorStateList getColorStateList(String name) {
        return getColorStateList(getIdentifierAttachSuffix(name, ResourceType.COLOR));
    }

    public String getString(String name) {
        return getString(getIdentifierAttachSuffix(name, ResourceType.STRING));
    }

    public int getIdentifierAttachSuffix(String name, @ResourceType String defType) {
        if (!TextUtils.isEmpty(mSuffix)) {
            name += mSuffix;
        }
        try {
            return getIdentifier(name, defType);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getIdentifier(String name, @ResourceType String defType) {
        int id = mResource.getIdentifier(name, defType, mPackageName);
        if (id == 0) {
            throw new IllegalArgumentException("没找到" + name);
        }

        return id;
    }
}
