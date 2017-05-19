package com.moetutu.acg12.entity;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description
 * Created by chengwanying on 2016/10/26.
 * Company BeiJing guokeyuzhou
 */
@JsonObject
@Entity
public class UserEntity {

    @Id(autoincrement = true)
    @JsonField
    public Long ID;
    
    @JsonField
    public String token;
    @JsonField
    public int uid;
    @JsonField
    public String name;
    @JsonField
    public String url;
    @JsonField
    public String avatarUrl;
    @JsonField
    public int point;
    @Generated(hash = 1781388735)
    public UserEntity(Long ID, String token, int uid, String name, String url,
            String avatarUrl, int point) {
        this.ID = ID;
        this.token = token;
        this.uid = uid;
        this.name = name;
        this.url = url;
        this.avatarUrl = avatarUrl;
        this.point = point;
    }
    @Generated(hash = 1433178141)
    public UserEntity() {
    }
    public Long getID() {
        return this.ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public int getUid() {
        return this.uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getAvatarUrl() {
        return this.avatarUrl;
    }
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    public int getPoint() {
        return this.point;
    }
    public void setPoint(int point) {
        this.point = point;
    }
  
   

}
