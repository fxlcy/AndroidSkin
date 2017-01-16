package cn.fxlcy.androidskin.annotation;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static cn.fxlcy.androidskin.annotation.ResourceType.*;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Created by fxlcy on 2016/11/4.
 */
@StringDef({ANIM, ARRAY, BOOL, COLOR, DECLARE_STYLEABLE, DRAWABLE, DIMEN, ID, INTEGER,
        LAYOUT, MIPMAP, STYLE, STRING})
@Retention(RetentionPolicy.SOURCE)
@Target({METHOD, PARAMETER, FIELD, LOCAL_VARIABLE})
public @interface ResourceType {
    String ANIM = "anim";
    String ARRAY = "array";
    String BOOL = "bool";
    String COLOR = "color";
    String DECLARE_STYLEABLE = "declare-styleable";
    String DRAWABLE = "drawable";
    String DIMEN = "dimen";
    String ID = "id";
    String INTEGER = "integer";
    String LAYOUT = "layout";
    String MIPMAP = "mipmap";
    String STYLE = "style";
    String STRING = "string";
}
