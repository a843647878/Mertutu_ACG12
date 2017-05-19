package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/3
 * version
 */
@JsonObject
public class LoginInfo {
    @JsonField
    public String token;
    @JsonField
    public UserEntity user;
    @JsonField
    public Easemob easemob;
    @JsonField
    public String lastApproval;
    @JsonField
    public int hasCompleted;

    @JsonObject
    public static class Easemob {
        @JsonField
        public String username;
        @JsonField
        public String password;
    }

    @JsonField
    public String ppvUrl;
}
