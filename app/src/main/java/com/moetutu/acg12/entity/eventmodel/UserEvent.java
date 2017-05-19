package com.moetutu.acg12.entity.eventmodel;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.moetutu.acg12.entity.UserEntity;

/**
 * Description
 * Created by chengwanying on 2017/3/31.
 * Company BeiJing kaimijiaoyu
 */
@JsonObject
public class UserEvent {
    @JsonField
    public UserEntity user;

    public UserEvent(UserEntity user) {
        this.user = user;
    }

    public UserEvent() {
    }
}
