package com.moetutu.acg12.asynctask.type;

import java.io.Serializable;
import java.util.List;


public class AcgArrayObj implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1913075680385598564L;
	private List<Acg12Obj> posts;
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Acg12Obj> getPosts() {
		return posts;
	}
	public void setPosts(List<Acg12Obj> posts) {
		this.posts = posts;
	}
}
