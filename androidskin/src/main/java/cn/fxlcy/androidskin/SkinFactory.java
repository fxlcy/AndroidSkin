package cn.fxlcy.androidskin;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import cn.fxlcy.androidskin.callback.LoadSkinResourceCallback;
import cn.fxlcy.androidskin.config.SkinSpConfig;

/**
 * Created by fxlcy on 2016/11/4.
 */

public class SkinFactory {
    private Context mContext;
    private String mPackageName = null;

    private static Method sAddAssetPathMethod;

    private static final int WHAT_ERROR = 0;
    private static final int WHAT_COMPLETE = 1;
    private LoadSkinResourceCallback mCallback;

    private Handler mSkinLoadHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            synchronized (this) {
                switch (msg.what) {
                    case WHAT_ERROR:
                        Exception e = (Exception) msg.obj;
                        mCallback.onError(e);
                        break;
                    case WHAT_COMPLETE:
                        mCallback.onComplete(SkinManager.getInstance());
                        SkinManager.getInstance().notificationSkinChanged();
                        break;
                }

                return true;
            }
        }
    });

    private SkinFactory() {
    }

    public static SkinFactory from(Context context) {
        SkinFactory factory = new SkinFactory();
        factory.mContext = context;
        factory.mPackageName = SkinSpConfig.getInstance(context).getSkinPackageName();

        return factory;
    }

    public void loadSkinResource(final String path, final LoadSkinResourceCallback callback) {
        loadSkinResource(path, null, callback);
    }

    public void loadSkinResource(final String path, final String pkgName, final LoadSkinResourceCallback callback) {
        mCallback = callback;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadSkinResource(path, pkgName);
                    mSkinLoadHandler.obtainMessage(WHAT_COMPLETE).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = mSkinLoadHandler.obtainMessage(WHAT_ERROR);
                    msg.obj = e;
                    msg.sendToTarget();
                }
            }
        }).start();
    }

    public SkinManager loadSkinResource(String path) throws Exception {
        return loadSkinResource(path, mPackageName);
    }

    public SkinManager loadSkinResource(String path, String pkgName) throws Exception {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("skinFilePath == NULL");
        }
        File f;
        if (!(f = new File(path)).exists() || !f.isFile()) {
            throw new FileNotFoundException(path);
        }

        if (!TextUtils.isEmpty(mPackageName)) {
            mPackageName = pkgName;
        } else {
            mPackageName = getPackageInfo(path).packageName;
        }

        Class<AssetManager> amClass = AssetManager.class;
        if (sAddAssetPathMethod == null) {
            sAddAssetPathMethod = amClass.getMethod("addAssetPath", String.class);
        }

        if (sAddAssetPathMethod == null) {
            throw new NoSuchMethodException("没找到AssetManager.addAssetPath()");
        }

        AssetManager assetManager = amClass.newInstance();
        sAddAssetPathMethod.invoke(assetManager, path);

        Resources resources = new Resources(assetManager
                , mContext.getResources().getDisplayMetrics()
                , mContext.getResources().getConfiguration());
        SkinSpConfig spConfig = SkinSpConfig.getInstance(mContext);
        spConfig.setIsUseSkin(true);

        return SkinManager.getInstance(new ResourceManager(resources, mPackageName
                , spConfig.getSkinSuffix()));
    }

    private PackageInfo getPackageInfo(String path) {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info == null) {
            throw new IllegalArgumentException(path + "皮肤文件加载失败");
        } else {
            return info;
        }
    }
}
