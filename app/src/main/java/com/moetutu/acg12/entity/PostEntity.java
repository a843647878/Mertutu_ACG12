package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2016/10/28.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
public class PostEntity {
    @JsonField
    public ArticleEntity post;
    @JsonField
    public List<ArticleEntity> posts;
}
