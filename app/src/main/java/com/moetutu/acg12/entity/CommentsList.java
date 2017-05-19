package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2017/4/1.
 * Company BeiJing kaimijiaoyu
 */
@JsonObject
public class CommentsList {

    @JsonField
    public List<CommentEntity> comments;

    @JsonField
    public int pages;
}
