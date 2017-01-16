package cn.fxlcy.androidskin.config;

/**
 * Created by fxlcy on 2016/11/5.
 * 对view的attr根据name进行过滤
 */
public interface SkinAttrFilter {
    /**
     * @return true代表通过，false代表不通过
     * @param resName 资源名
     * */
    boolean filter(String resName);
}
