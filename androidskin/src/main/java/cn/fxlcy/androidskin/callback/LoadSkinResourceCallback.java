package cn.fxlcy.androidskin.callback;


import cn.fxlcy.androidskin.SkinManager;

/**
 * Created by fxlcy on 2016/11/6.
 * 异步加载皮肤资源的回调
 */
public interface LoadSkinResourceCallback {
    void onError(Exception e);

    void onComplete(SkinManager skinManager);
}
