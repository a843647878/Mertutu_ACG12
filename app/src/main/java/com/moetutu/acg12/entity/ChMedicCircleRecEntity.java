package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * 中医界推荐
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/1
 * version
 */
@JsonObject
public class ChMedicCircleRecEntity {
    @JsonField
    public int id;
    @JsonField
    public String img;
    @JsonField
    public String title;
    @JsonField
    public String desc;

}
