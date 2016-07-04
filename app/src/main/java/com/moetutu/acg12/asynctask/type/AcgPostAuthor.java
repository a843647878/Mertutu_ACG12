package com.moetutu.acg12.asynctask.type;

import java.io.Serializable;

public class AcgPostAuthor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3587600048597968646L;
	private String name;
	private String avatar;
	private String description;
	private String url;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	

}
