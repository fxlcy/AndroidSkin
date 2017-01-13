package cn.fxlcy.androidskin.example;

import android.app.Application;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fxlcy
 * on 2017/1/13.
 *
 * @author fxlcy
 * @version 1.0
 */

public class BaseApp extends Application {

    public static String sSkinPath;

    @Override
    public void onCreate() {
        super.onCreate();

        InputStream is = null;
        FileOutputStream fos = null;

        try {
            is = getAssets().open("skin1.skin");
            sSkinPath = getDir("skin", MODE_PRIVATE).getAbsolutePath() + File.separator +
                    "skin.zip";
            fos = new FileOutputStream(sSkinPath);

            byte[] buffer = new byte[1024];

            while (true) {
                int readLen = is.read(buffer);
                if (readLen <= 0) {
                    break;
                }

                fos.write(buffer, 0, readLen);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ignored) {
            }
        }
    }
}
