package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2016/12/21.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
public class StorageEntity {
    @JsonField
    public List<DonwLoadEntity> items;
}
