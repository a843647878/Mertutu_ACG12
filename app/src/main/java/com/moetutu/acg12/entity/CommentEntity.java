package com.moetutu.acg12.entity;

import com.google.gson.annotations.SerializedName;
import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class CommentEntity {

	@JsonField
	private String date;

	@JsonField
	private String humanDate;

	@JsonField
	private int ID;

	@JsonField
	private UserEntity user;

	@JsonField
	private String content;

	@JsonField
	private int parentId;

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	@JsonField
	public List<CommentEntity> children;

	public void setDate(String date){
		this.date = date;
	}

	public String getDate(){
		return date;
	}

	public void setHumanDate(String humanDate){
		this.humanDate = humanDate;
	}

	public String getHumanDate(){
		return humanDate;
	}



	public void setUser(UserEntity user){
		this.user = user;
	}

	public UserEntity getUser(){
		return user;
	}

	public void setContent(String content){
		this.content = content;
	}

	public String getContent(){
		return content;
	}

	public void setParentId(int parentId){
		this.parentId = parentId;
	}

	public int getParentId(){
		return parentId;
	}

	@Override
 	public String toString(){
		return 
			"CommentEntity{" +
			"date = '" + date + '\'' + 
			",humanDate = '" + humanDate + '\'' + 
			",id = '" + ID + '\'' +
			",user = '" + user + '\'' + 
			",content = '" + content + '\'' + 
			",parentId = '" + parentId + '\'' + 
			"}";
		}
}