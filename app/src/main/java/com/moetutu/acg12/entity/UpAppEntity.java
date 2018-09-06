package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * Created by chengwanying on 2017/8/5.
 * Company BeiJing kaimijiaoyu
 */
@JsonObject
public class UpAppEntity {
    @JsonField
    public int versionCode;
    @JsonField
    public String url;
    @JsonField
    public String instructions;
}
