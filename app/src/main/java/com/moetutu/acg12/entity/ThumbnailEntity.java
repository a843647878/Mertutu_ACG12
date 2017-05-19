package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * Created by chengwanying on 2016/10/26.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
public class ThumbnailEntity {
    @JsonField
    public String url;
    @JsonField
    public int width;
    @JsonField
    public int height;

}
