package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * 更新包的信息
 * Company Beijing guokeyuzhou
 * author  chengwanying  E-mail:wanyingandroid@163.com
 * date createTime：16/6/21
 * version
 */
@JsonObject
public class UpdateApkInfo {

    @JsonField
    public int force;
    @JsonField
    public int update;
    @JsonField
    public int released;
    @JsonField
    public String updateInfo;
    @JsonField
    public String updateUrl;

}
