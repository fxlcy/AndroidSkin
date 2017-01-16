package cn.fxlcy.androidskin.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import cn.fxlcy.androidskin.ResourceManager;
import cn.fxlcy.androidskin.SkinManager;
import cn.fxlcy.androidskin.util.AppUtils;


/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinSpConfig {
    @SuppressLint("StaticFieldLeak")
    private static SkinSpConfig mInstance;

    private final static String KEY_SKIN_PKG_PATH = "skin_pkg_path";
    private final static String KEY_SKIN_PKG_NAME = "skin_pkg_name";
    private final static String KEY_SKIN_USE = "skin_is_use";
    /**
     * 资源文件的后缀，用来区分一个包中的多种皮肤
     */
    private final static String KEY_SKIN_SUFFIX = "skin_suffix";

    private SkinSpConfig() {
    }

    public static SkinSpConfig getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SkinSpConfig();
            if (context != null) {
                if (!context.equals(mInstance.mContext)) {
                    mInstance.mContext = AppUtils.getApplicationContext(context);//获得applicationContext
                    // ，防止Context内存泄漏

                    mInstance.mSp = mInstance.mContext.getSharedPreferences(SkinConfig.SKIN_SP_NAME
                            , Context.MODE_PRIVATE);
                }
            } else {
                if (mInstance.mContext == null) {
                    throw new NullPointerException("context == NULL");
                }
            }
        }
        return mInstance;
    }

    private SharedPreferences mSp;
    private Context mContext;


    public boolean isUseSkin() {
        return mSp.getBoolean(KEY_SKIN_USE, false);
    }

    public boolean setIsUseSkin(boolean isUse) {
        return mSp.edit().putBoolean(KEY_SKIN_USE, isUse).commit();
    }

    /**
     * 设置是否使用皮肤
     */
    public boolean putIsUseSkin(boolean isUseSkin) {
        return mSp.edit().putBoolean(KEY_SKIN_USE, isUseSkin).commit();
    }

    /**
     * 获得皮肤包的路径
     */
    public String getSkinPackagePath() {
        return mSp.getString(KEY_SKIN_PKG_PATH, null);
    }

    /**
     * 获得皮肤包的包名
     */
    public String getSkinPackageName() {
        return mSp.getString(KEY_SKIN_PKG_NAME, null);
    }

    /**
     * 保存皮肤包的路径
     */
    public boolean putSkinPackagePath(String path) {
        return mSp.edit().putString(KEY_SKIN_PKG_PATH, path).commit();
    }

    /**
     * 保存皮肤包的包名
     */
    public boolean putSkinPackageName(String pkgName) {
        return mSp.edit().putString(KEY_SKIN_PKG_NAME, pkgName).commit();
    }


    public String getSkinSuffix() {
        return mSp.getString(KEY_SKIN_SUFFIX, null);
    }

    public boolean putSkinSuffix(String suffix) {
        ResourceManager res = SkinManager.getInstance().getResourceManager();
        if (res != null) {
            res.setSuffix(suffix);
        }
        return mSp.edit().putString(KEY_SKIN_SUFFIX, suffix).commit();
    }


    public boolean clear() {
        return mSp.edit().clear().commit();
    }
}
