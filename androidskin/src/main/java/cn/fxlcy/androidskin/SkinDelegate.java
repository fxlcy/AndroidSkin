package cn.fxlcy.androidskin;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.LayoutInflaterCompat;
import android.view.LayoutInflater;

import cn.fxlcy.androidskin.callback.SkinChangedListener;
import cn.fxlcy.androidskin.config.SkinSpConfig;

/**
 * Created by fxlcy on 2016/11/6.
 */
public class SkinDelegate implements SkinChangedListener {
    private Context mContext;
    private SkinChangedListener mListener;

    public SkinDelegate(Activity context) {
        this(context, null);
    }

    public SkinDelegate(Activity context, SkinChangedListener listener) {
        mContext = context;
        if (listener != null) {
            mListener = listener;
        } else {
            mListener = this;
        }

        SkinLayoutInflaterFactory factory = new SkinLayoutInflaterFactory(mListener);
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutInflaterCompat.setFactory(inflater, factory);

        SkinManager.getInstance().addSkinChangedListener(mListener);
    }

    public SkinManager getSkinManager() {
        return SkinManager.getInstance();
    }

    public SkinSpConfig getSkinSpConfig() {
        return SkinSpConfig.getInstance(mContext);
    }

    public void onDestroy() {
        SkinManager.getInstance().removeSkinChangedListener(mListener);
        mContext = null;
    }

    @Override
    public void onSkinChanged() {

    }
}
