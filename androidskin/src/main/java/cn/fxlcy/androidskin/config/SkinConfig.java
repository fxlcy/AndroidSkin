package cn.fxlcy.androidskin.config;

/**
 * Created by fxlcy on 2016/11/5.
 */

public class SkinConfig {
    private final static String ATTR_PREFIX = "skin";
    /**
     * 配置文件SharedPreferences文件名
     * */
    public final static String SKIN_SP_NAME = "skin";

    public static SkinAttrFilter SKIN_ATTR_FILTER = new SkinAttrFilter() {
        @Override
        public boolean filter(String attrName) {
            return attrName.startsWith(ATTR_PREFIX);
        }
    };
}
