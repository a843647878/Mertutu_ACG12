package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

/**
 * Description
 * Created by chengwanying on 2016/10/26.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
public class ArticleEntity {
    @JsonField
    public int id;
    @JsonField
    public String title;
    @JsonField
    public String url;
    @JsonField
    public ThumbnailEntity thumbnail;
    @JsonField
    public int comment;
    @JsonField
    public String excerpt;
    @JsonField
    public String content;
    @JsonField
    public UserEntity author;
    @JsonField
    public DateEntity date;
    @JsonField
    public StorageEntity storage;

}
