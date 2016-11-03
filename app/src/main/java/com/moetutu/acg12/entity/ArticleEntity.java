package com.moetutu.acg12.entity;

/**
 * Description
 * Created by chengwanying on 2016/10/26.
 * Company BeiJing guokeyuzhou
 */

public class ArticleEntity {
    public int id;
    public String title;
    public String url;

    public ThumbnailEntity thumbnail;
    public int comment;

    public String content;
    public UserEntity author;

}
