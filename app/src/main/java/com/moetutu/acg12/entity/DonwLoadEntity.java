package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * Created by chengwanying on 2016/12/21.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
public class DonwLoadEntity {
    @JsonField
    public String url;
    @JsonField
    public String name;
    @JsonField
    public String downloadPwd;
    @JsonField
    public String extractPwd;
}
